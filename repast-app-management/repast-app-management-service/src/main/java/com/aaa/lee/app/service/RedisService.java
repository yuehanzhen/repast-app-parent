package com.aaa.lee.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

/**
 * @Company AAA软件教育
 * @Author Seven Lee
 * @Date Create in 2019/11/21 9:53
 * @Description
 **/
@Service
public class RedisService {

    @Autowired
    private JedisCluster jedisCluster;

    /**
     * @author Seven Lee
     * @description
     *      向redis中取出数据
     * @param [key]
     * @date 2019/11/21
     * @return java.lang.String
     * @throws 
    **/
    public String get(String key) {
        return jedisCluster.get(key);
    }

    /**
     * @author Seven Lee
     * @description
     *      从redis中存入数据
     * @param [key, value]
     * @date 2019/11/21
     * @return java.lang.String
     * @throws 
    **/
    public String set(String key, String value) {
        return jedisCluster.set(key, value);
    }

    /**
     * 根据key值删除数据
     */
    public Long delect(String key){
        return jedisCluster.del(key);
    }

    /**
     * 设置失效时间
     */

}
