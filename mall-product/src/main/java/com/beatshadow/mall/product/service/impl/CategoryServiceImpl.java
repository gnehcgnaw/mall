package com.beatshadow.mall.product.service.impl;

import com.beatshadow.common.valid.ListValue;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.common.utils.Query;

import com.beatshadow.mall.product.dao.CategoryDao;
import com.beatshadow.mall.product.entity.CategoryEntity;
import com.beatshadow.mall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

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