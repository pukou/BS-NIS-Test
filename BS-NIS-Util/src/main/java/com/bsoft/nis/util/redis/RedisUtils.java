package com.bsoft.nis.util.redis;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Jedis;

/**
 * Describtion:redis工具类
 * Created: dragon
 * Date： 2017/1/19.
 */
public class RedisUtils {
    /**
     * 从工厂中获取redis实例
     * @param factory
     * @return
     */
    public static Jedis getRedisClient(JedisConnectionFactory factory){
        return factory.getShardInfo().createResource();
    }
}
