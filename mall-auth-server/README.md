分布式session的问题:
    1. 复制
    2. 客户端存储
    3. hash一致性 ，选择机器 【宕机session丢失、水平扩张求余重新hash】
    4. session统一存储【redis】
    5. 子域名的作用域问题 【放大session作用域名的范围】tomcat是自动设置为子域名
    6. Spring session 将 session存放到redis,并将子域名session作用域放大。
单点登录：
    1、中央认证服务器；
    2、其他系统都要去中央服务器登录，登录成功跳转回来
    3、只要一个登录，其他都不需要登录
    4、全系统统一一个cookie，这些服务都不是同一个域名下的。    
 