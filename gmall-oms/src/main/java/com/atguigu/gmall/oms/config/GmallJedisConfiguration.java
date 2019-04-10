package com.atguigu.gmall.oms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author ：mei
 * @date ：Created in 2019/3/29 0029 下午 20:35
 * @description：Jedis配置类
 * @modified By：
 * @version: $
 */
@Configuration
public class GmallJedisConfiguration {

    @Value("${spring.redis.host:127.0.0.1}")
    private String host;
    @Value("${spring.redis.port:6379}")
    private Integer port;
    @Value("${spring.redis.jedis.timeout:3000}")
    private Integer timeout;
    /**
     * spring.redis.jedis.pool.max-idle=8
     * spring.redis.jedis.pool.min-idle=0
     * spring.redis.jedis.pool.total=100
     */

    @Value("${spring.redis.jedis.pool.max-idle:8}")
    private Integer maxIdle;

    @Value("${spring.redis.jedis.pool.total:8}")
    private Integer maxTotle;

    @Value("${spring.redis.jedis.pool.min-idle:0}")
    private Integer minIdle;


    /**
     * 1）、要么用JedisPool要么用RedisTemplate
     * 2）、只想要JedisPool 移除spring-data-redis-starter
     *
     * @return
     */
    @Bean
    public JedisPool jedisPoolConfig() {
        //1、连接工厂中所有信息都有。
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxTotle);
        config.setMinIdle(minIdle);

        JedisPool jedisPool = new JedisPool(config, host, port, timeout);

        return jedisPool;
    }
}
