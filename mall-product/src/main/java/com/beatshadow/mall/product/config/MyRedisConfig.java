package com.beatshadow.mall.product.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/23 02:06
 */
@EnableConfigurationProperties(CacheProperties.class)
@Configuration
@EnableCaching //开启缓存
public class MyRedisConfig {
    //配置文件中的东西没有使用到
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties){
        CacheProperties.Redis cachePropertiesRedis = cacheProperties.getRedis();

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig().
                //设置key的序列化器
                // 设置key的序列化为string
                        serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                //设置value的序列化器
                //设置value的是FastJson的json序列化方式
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()));

        if (cachePropertiesRedis.getTimeToLive()!=null){
            redisCacheConfiguration = redisCacheConfiguration.entryTtl(cachePropertiesRedis.getTimeToLive());
        }
        if (cachePropertiesRedis.getKeyPrefix()!=null){
            redisCacheConfiguration = redisCacheConfiguration.prefixKeysWith(cachePropertiesRedis.getKeyPrefix());
        }
        //缓存穿透问题
        if (!cachePropertiesRedis.isCacheNullValues()){
            redisCacheConfiguration = redisCacheConfiguration.disableCachingNullValues();
        }
        if (!cachePropertiesRedis.isUseKeyPrefix()){
            redisCacheConfiguration = redisCacheConfiguration.disableKeyPrefix() ;
        }
        return redisCacheConfiguration ;
    }

}
