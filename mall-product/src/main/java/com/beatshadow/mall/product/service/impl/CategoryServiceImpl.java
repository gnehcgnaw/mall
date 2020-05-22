package com.beatshadow.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.beatshadow.mall.product.service.CategoryBrandRelationService;
import com.beatshadow.mall.product.vo.Catalog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.common.utils.Query;

import com.beatshadow.mall.product.dao.CategoryDao;
import com.beatshadow.mall.product.entity.CategoryEntity;
import com.beatshadow.mall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    final
    CategoryBrandRelationService categoryBrandRelationService;

    final
    StringRedisTemplate stringRedisTemplate ;

    final RedissonClient redissonClient ;


    public CategoryServiceImpl(CategoryBrandRelationService categoryBrandRelationService, StringRedisTemplate stringRedisTemplate, RedissonClient redissonClient) {
        this.categoryBrandRelationService = categoryBrandRelationService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redissonClient = redissonClient;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> queryTree() {
        //1. 查询数据
        List<CategoryEntity> categoryEntityList = this.list();
        //2. 改造成tree

        List<CategoryEntity> level1Menus = categoryEntityList.stream().filter((categoryEntity -> {
            //2.1. 返回root
            return categoryEntity.getParentCid() == 0;
            //2.3. 对得到的菜单进行后续操作
        })).map((menus)->{
            //2.3.1. 设置children
            menus.setChildren(getChildrenList(menus,categoryEntityList));
            return menus ;
            //2.4.排序
        }).sorted((menus1,menus2)->{
            return (menus1.getSort() == null?0:menus1.getSort()) -(menus2.getSort()==null ? 0:menus2.getSort());
        }).collect(Collectors.toList());//2.2. 将得到的记过以集合的形式返回
        return level1Menus;
    }

    @Override
    public void removeMenuByIds( List<Long> catIds) {

        //todo  检查当前要删除的菜单，是否被别的地方应用，

        //批量删除[这个是物理删除]，而开发中应该采用的是逻辑删除，show_status
        baseMapper.deleteBatchIds(catIds);
    }

    //[2,25,225]
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        //逆转
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级联更新所有关联的数据
     * @param category
     */

     // @Caching 组合模式
/*    @Caching(evict = {
            @CacheEvict(cacheNames = {"category"},key = "'level1Categorys'") // 失效模式
            })*/
    @CacheEvict(value ={"category"} ,allEntries = true ) //  删除某个分区中的所有数据
        // @CachePut 双写模式
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    @Override
    public CategoryEntity selectById(Long catelogId) {
        return baseMapper.selectById(catelogId);
    }

    //缓存分区【跟业务有关系】
    //表示当前方法的结果进行缓存，如果缓存中有，方法调用就不执行，如果缓存没有，就执行方法，然后缓存
    @Cacheable(cacheNames = {"category"},key = "'level1Categorys'"
           // ,sync = true 默认无锁状态， 可以将这个值设置为true， 防止缓存击穿，放过一部分去差数据库是没有问题的，
    ) //指定过期时间在配置文件中指定
    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid",0));
    }

    /**
     * 这部分数据不经常变，所以可以引入缓存
     *      使用缓存的场景：
     *          1、及时性、数据一致性要求不高 【物流信息、商品分类】
     *          2、访问量大且更新频率不高的数据（读多，写少）【商品数据】
     *
     *          分布式情况使用分布式中间件，而不是把缓存放在自己的进程内。
     *
     * 高并发下缓存失效问题：
     *  1、缓存穿透 【查询一个一定不存在的数据，就会产生缓存穿透】
     *       举例：  100万并发 ---》查询数据没有的商品， 就都查询数据库，这时候数据库承受不了，
     *           【解决方案：null结果缓存，并加入短时过期时间（包装新数据可以查到）】
     *  2、缓存血崩 【存储的数据，设置了相同的失效时间，在一时间key大面积同时失效】
     *      【解决方案：设置不同的失效时间】
     *  3、缓存击穿【高并发，失效的一刻热点数据刚好失效】
     *      【解决方案：加锁】保证只有一个先查询，后续查询缓存
     *
     *
     *      缓存分为：
     *              1、双写模式
     *              2、失效模式
     *              缓存一致性问题，只能保证最终一致性
     *            Canal 中间件，   把自己伪装成从服务器，  异构问题，推荐，实时订阅访问记录计算出感兴趣的表，生产推荐信息表，数据异构问题
     * @return
     */
    @Override
    public Map<String, List<Catalog2Vo>> getCatalogJson() {

       /* getLevel1Categorys().stream().map((item)->{
            item.get
        })*/
/*
        List<CategoryEntity> level1Categorys = getLevel1Categorys();

        Map<String, List<Catalog2Vo>> collect = level1Categorys.stream().collect(Collectors.toMap((k) -> { return k.getCatId().toString(); }, (v) -> {
            //
            List<CategoryEntity> categoryEntityList = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
            //做非空判断
            List<Catalog2Vo> catalog2Vos = null;
            if (categoryEntityList != null) {
                catalog2Vos = categoryEntityList.stream().map((l2) -> {
                    //
                    List<CategoryEntity> categoryLevel3 = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", l2.getCatId()));

                    List<Catalog2Vo.Catalog3Vo> catalog3Vos = null;
                    if (categoryLevel3 != null) {
                        catalog3Vos = categoryLevel3.stream().map((l3) -> {
                            Catalog2Vo.Catalog3Vo catalog3Vo = Catalog2Vo.Catalog3Vo.builder()
                                    .catalog2Id(l2.getCatId().toString())
                                    .id(l3.getCatId().toString())
                                    .name(l3.getName()).build();
                            return catalog3Vo ;
                        }).collect(Collectors.toList());

                    }
                    Catalog2Vo catalog2Vo = Catalog2Vo.builder().catalog1Id(v.getCatId().toString())
                            .catalog3List(catalog3Vos)
                            .id(l2.getCatId().toString())
                            .name(l2.getName()).build();

                    return catalog2Vo;
                }).collect(Collectors.toList());
            }
            return catalog2Vos ;
        }));
        return collect;*/

        //「以上代码是嵌套查询」优化操作 一次查出来，然后在查出来的结果上筛选

        /**
         * 1、空结果缓存，解决缓存穿透问题
         * 2、设置过期时间（加随机值），解决缓存雪崩
         *          前两个好解决
         *
         * 3、加锁，解决缓存击穿 [都尝试去一个地方站坑位 ，]
         *      http://www.redis.cn/commands/setnx.html
         *      http://www.redis.cn/commands/set.html
         */
        //2、查询缓存
        String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
        if (StringUtils.isEmpty(catalogJSON)){
            //都存储json，比如Java，PHP序列化数据不同，【为了跨语言，跨平台，使用json】
            //Map<String, List<Catalog2Vo>> catalogJsonFromDb = getCatalogJsonFromDb();
            //Map<String, List<Catalog2Vo>> catalogJsonFromDb = getCatalogJsonFromDbWithRedisLock();
            Map<String, List<Catalog2Vo>> catalogJsonFromDb =  getCatalogJsonFromDbWithRedssion();
            String s = JSON.toJSONString(catalogJsonFromDb);
            stringRedisTemplate.opsForValue().set("catalogJSON",s);
            log.debug("查询缓存");
            return catalogJsonFromDb ;
        }else {
            Map<String, List<Catalog2Vo>> map = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catalog2Vo>>>() {
            });
            return map ;
        }

    }

    //org.apache.http.TruncatedChunkException: Truncated chunk 【jmetr偶尔出现这个错误】
    //解决方案：在nginx中关闭 proxy_buffering off;
    private Map<String, List<Catalog2Vo>> getCatalogJsonFromDbWithRedssion() {
        Map<String, List<Catalog2Vo>> catalogJsonFromDb = null ;
        RLock lock = redissonClient.getLock("lock");
        lock.lock();
        try{
            log.debug("获取锁");
            return getCatalogJsonFromDb();
        }finally {
            log.debug("释放锁");
            lock.unlock();
        }

    }

    //分布式锁
    private Map<String, List<Catalog2Vo>> getCatalogJsonFromDbWithRedisLock(){
        //抢占分布式锁，站坑
        // [nx] 对应 setIfAbsent

        //  Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", "catalogJsonLock");
        String uuid = UUID.randomUUID().toString();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);
        Map<String, List<Catalog2Vo>> catalogJsonFromDb = null ;
        if (lock){
            try{
                log.debug("{},获取到锁",Thread.currentThread().getName());
                //在不想做自动续锁的情况下，将失效时间设置长一点，然后finally统一解锁
                catalogJsonFromDb = getCatalogJsonFromDb();

            }catch (Exception e){
                log.error("Exception is {}",e.getMessage());
            } finally {
                //加锁成功
                //如果以下代码出现异常，锁没被删除【即：如果解锁失败，会造成死锁】：优化：获取到锁之后设置过期时间，
                // stringRedisTemplate.expire("lock",30,TimeUnit.SECONDS);  //过期时间没设置成功出现断情况，锁还是没有被解锁

                //出现上述问题，其实就是获取锁和设置过期时间不在同一个原子操作:set lock catalogJsonLock EX 300 NX

                //执行完业务之后，要删除锁
                //stringRedisTemplate.delete("lock"); //业务执行时间过长，自己的锁早过期了，把别人的锁删除了，【解决方案这是value =uuid】
          /*  String lockValue = stringRedisTemplate.opsForValue().get("lock");
            if (uuid.equals(lockValue)){
                stringRedisTemplate.delete("lock");
            }*/

                //以下解决方案又是非原子性质的操作【官方说明了问题的存在，给出了解决方案 ，需要执行一段Lua的脚本】
                String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else  return 0 end";
                // 1, 删除成功，2， 删除失败 【但是值就没必要接收了】(脚本返回的是null)
                Long execute = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);
                log.debug("{},释放锁{}",Thread.currentThread().getName(),execute);
            }
            return catalogJsonFromDb ;
        } else {
            //加锁失败，也要返回数据，等待100毫秒，要重试
            //休眠
            try {
                log.debug("{},加锁失败,等待重试",Thread.currentThread().getName());
                //防止内存溢出
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getCatalogJsonFromDbWithRedisLock();
        }

    }

    private Map<String, List<Catalog2Vo>> getCatalogJsonFromDb() {

        //1. 查出所有，在这个基础上再次查询
        List<CategoryEntity> categoryEntityList = baseMapper.selectList(null);

        List<CategoryEntity> level1Categorys = getLevel1Categorys();

        Map<String, List<Catalog2Vo>> collect = level1Categorys.stream().collect(Collectors.toMap((k) -> { return k.getCatId().toString(); }, (v) -> {
            //
           // List<CategoryEntity> categoryEntityList = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
            //做非空判断
            List<Catalog2Vo> catalog2Vos = null;
            if (categoryEntityList != null) {
                catalog2Vos = categoryEntityList.stream().map((l2) -> {
                    //
                    List<CategoryEntity> categoryLevel3 = getParent_cid(categoryEntityList,l2.getCatId());

                    List<Catalog2Vo.Catalog3Vo> catalog3Vos = null;
                    if (categoryLevel3 != null) {
                        catalog3Vos = categoryLevel3.stream().map((l3) -> {
                            Catalog2Vo.Catalog3Vo catalog3Vo = Catalog2Vo.Catalog3Vo.builder()
                                    .catalog2Id(l2.getCatId().toString())
                                    .id(l3.getCatId().toString())
                                    .name(l3.getName()).build();
                            return catalog3Vo ;
                        }).collect(Collectors.toList());

                    }
                    Catalog2Vo catalog2Vo = Catalog2Vo.builder().catalog1Id(v.getCatId().toString())
                            .catalog3List(catalog3Vos)
                            .id(l2.getCatId().toString())
                            .name(l2.getName()).build();

                    return catalog2Vo;
                }).collect(Collectors.toList());
            }
            return catalog2Vos ;
        }));
        return collect ;
    }

    /**
     * 然后在结果上筛选
     * @param categoryEntityList
     * @param cid
     * @return
     */
    private List<CategoryEntity> getParent_cid(List<CategoryEntity> categoryEntityList  ,Long cid) {
        List<CategoryEntity> collect = categoryEntityList.stream().filter((item) -> {
            return item.getParentCid() == cid;
        }).collect(Collectors.toList());
        return collect ;
        //return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", l2.getCatId()));
    }


    //225,25,2
    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        //1、收集当前节点id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        //递归收集
        if(byId.getParentCid()!=0){
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;
    }

    /**
     * 获取孩子菜单
     * @param root    当前节点
     * @param categoryEntityList    要查的数据集合
     * @return
     */
    private List<CategoryEntity> getChildrenList(CategoryEntity root , List<CategoryEntity> categoryEntityList) {
        List<CategoryEntity> childrenCategoryEntityList = categoryEntityList.stream().filter(((categoryEntity) -> {
            //返回的是当前的父节点等于传过来的分类ID
            return categoryEntity.getParentCid() == root.getCatId();
        })).map((menus)->{
            menus.setChildren(getChildrenList(menus,categoryEntityList));
            return menus ;
        }).sorted((menus1, menus2) -> {
            return (menus1.getSort() == null ? 0 : menus1.getSort()) - (menus2.getSort() == null ? 0 : menus2.getSort());
        }).collect(Collectors.toList());
        return childrenCategoryEntityList ;
    }

}