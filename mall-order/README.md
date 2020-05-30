1. 电商系统的三流：
   1、信息流
   2、资金流
   3、物流
   
2. 而订单系统作为中枢将三者有机结合在一起，这个环节需要多个模块的数据和信息，同时对这些信息进行加工处理后流向下一个环节。

3. 订单中心：
   1、用户信息
   2、订单信息
   3、商品信息
   4、物流信息
   5、支付信息
   6、促销信息
   
4. 订单的状态：
   1、代付款【长时间未支付要重置库存锁定】
   2、已发货、待发货 【仓库系统】
   3、待收货、已发货【订单系统同步物流信息】
   4、已完成
   5、已取消
   6、售后中
   订单的流程
   1、虚拟订单
   2、实物订单

5. 接口幂等性
   例如：提交订单 ，保证提交订单的幂等性
   场景：支付，提交订单

   * 那些情况需要防止
     1、用户多次点击按钮
     2、用户页面回退再提交
     3、微服务互相调用，由于网络问题，导致请求失败，feign触发重试机制
     4、其他业务情况
   * 什么情况下需要幂等

     * 以数据库为例，有些操作是天然幂等的，
       * 查询数据
       * 固定更新【某一个字段的更新】
       * 删除
       * 带主键的插入
     * 不是幂等的
       * 运算修改，对某一个字段  +1
       * 不带主键的插入
     * 非主键唯一性，【添加索引】
   * 幂等性的解决方案

     * token机制【验证码】
       * 服务器端是先删，还是后删？ **获取、对比、删除 必须是原子性操作才行**
       * 最终 redis的lua脚本，保证其原子性。
     * 各种锁机制
       * 数据库的悲观锁
         * select * form  where id = 1 for update 
         * id必须是主键或者唯一索引，不让可能造成锁表
       * 数据库的乐观锁【适合于读多写少】
         * 更新带上version，where 携带version更新
     * 分布式锁
     * 各种唯一约束
       * 数据库的唯一约束 【订单号唯一】
       * redis set 防重 【MD5值唯一】
     * 防重表
     * 全局请求唯一ID 
       * ----》nginx ---->链路追踪
   * 订单提交的幂等性【使用token方式】

   # 本地事务

   ## 事务的基本性质

   1. 原子性：一些列的操作整体是不可拆分的、要么同时成功，要么同时失败。
   2. 一致性：业务前后的重量是一致的，【转账】
   3. 隔离性：一百个人同时下单，一个人失败了，不会影响其他人。
   4. 持久性：一旦事务成功，就落盘。

   本地事务，就是操作连接的是同一个数据库，使用同一条连接，在这条连接里发了三个SQL ,第一个改库存、第二个改订单、第三个改积分，只要一个发生错误，就回滚，使用Spring的一个注解：`@Transactional`

   ## 事务的隔离级别

   1. 读未提交：其实就是未提交已经被别的事务读到了数据。【脏读】
   2. 读已提交：可以读到别人已经提交的数据。【oracle 、serverSQL】
   3. 可重复读：在整个事务期间，读同一条数据，不管这个数据是否被其他事务修改，读到的数据是一样的【幻读】
   4. 序列化：事务都是串行顺序执行的，从而避免脏读、不可重复读、幻读的问题。【没有并发能力】

   # 事务的传播行为

   <img src='http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-29-o66wMy.png' alt='2020-05-29-o66wMy'/>

   

   <img src='http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-29-lkSJwB.png' alt='2020-05-29-lkSJwB' style="zoom:50%;" />

   A事务的所有的设置都传播到了B，B出了设置了传播行为之外，设置了其他没有作用。

   Springboot ，同一service，设置事务的传播机制是没用的，因为**事务使用过的是代理对象来控制的**。【调用就相当于复制粘贴，跳过了代理对象】

   如何解决呢？本地事务失效（统一service中的事务设置是没用的），解决方式：使用代理对象使用事务，引入aop，使用aspect j，开启aspectj动态代理，【默认使用的是jdk的动态代理】

   <img src='http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-29-bTrOeh.png' alt='2020-05-29-bTrOeh'/>

   然后使用代理对象回调：

   ![image-20200529195226980](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-29-Jx02tH.png)

   # 分布式事务

   ## 为什么会有分布式事务

   <img src='http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-29-dGYc80.png!github_blog' alt='2020-05-29-dGYc80'/>

## CAP定理与BASE理论

<img src='http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-29-ojC44i.png!github_blog' alt='2020-05-29-ojC44i'/>

![image-20200529200530323](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-29-mEUU0h.png!github_blog)

* 分布式要么CP 、要么AP。
* 分布式系统都要满足CAP定理。
* 分布式系统中实现一致性的raft算法、paxos
  * 演示：http://thesecretlivesofdata.com/raft/【zookeeper】
    * node:	随从、候选者、领导者。
      * 随从没有收到领导者消息，会被自己变成候选人。【领导的选举】

## 面临的问题

![image-20200529211739512](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-29-w51k5O.png!github_blog)

## BASE理论

![image-20200529211813921](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-29-elG4Dd.png!github_blog)

![image-20200529211918898](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-29-rMt9SH.png!github_blog)

![image-20200529212053189](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-29-3r8cVT.png!github_blog)

## 强一致性、弱一致性、最终一致性

![image-20200529212249626](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-29-abxRKN.png!github_blog)

# 分布式事务几种方案

## 刚性事务——2PC模式

![image-20200529212445039](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-29-YW7NbA.png!github_blog)

<img src="http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-29-nnrohO.png!github_blog" alt="image-20200529212535646" style="zoom:50%;" />

![image-20200529212730840](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-29-XNGSNk.png!github_blog)

## 柔性事务——TCC事务补偿方案

![image-20200529212933303](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-29-rTadZN.png!github_blog)

## 柔性事务——最大努力通知型

![image-20200529213301705](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-29-ZVviIL.png!github_blog)

## 柔性事务——可靠消息+最终一致性方案（异步确保型）

![image-20200529213502996](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-29-uPfRoE.png!github_blog)

# Seata的使用

SEATA提供了AT(自动提交事务)、TCC 、SAGA和XA事务模式。

## 快速开始

http://seata.io/zh-cn/docs/user/quickstart.html

术语：TC TM RM

![img](http://seata.io/img/solution.png)

https://github.com/seata/seata



高并发场景使用：

* 柔性事务——可靠消息+最终一致性方案（异步确保型）
* 柔性事务——最大努力通知型

项目中，

* 后台管理spuinfo的时候，使用seata的At模式 ，

* 下订单使用失败发消息给库存服务，也可以让库存本身使用自动解锁

<img src="http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-30-tmf5f6.png!github_blog" alt="image-20200530005854167" style="zoom:67%;" />

<img src="http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-30-KRPVOd.png!github_blog" alt="image-20200530005922155" style="zoom:67%;" />

![image-20200530010007923](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-30-W2XG2O.png!github_blog)

![image-20200530010453590](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-30-mNRADL.png!github_blog)

![image-20200530010637976](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-30-KFXAqE.png!github_blog)

![image-20200530011633431](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-30-sK1cWz.png!github_blog)

# 支付模块

![image-20200530014057080](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-30-bjghVw.png!github_blog)

## 公钥、私钥？

![image-20200530014337739](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-30-CeKqAU.png!github_blog)

![image-20200530014810215](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-30-iTprbZ.png!github_blog)

## 使用沙箱环境

异步通知，通过内网穿透软件测试的时候出现问题，

<img src='http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-30-qNfC0C.png!github_blog' alt='2020-05-30-qNfC0C'/>

![image-20200530153052304](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-30-NT08y7.png!github_blog)

![image-20200530161526945](http://gnehcgnaw.oss-cn-hongkong.aliyuncs.com/blog/2020/05/2020-05-30-AA0AWh.png!github_blog)

