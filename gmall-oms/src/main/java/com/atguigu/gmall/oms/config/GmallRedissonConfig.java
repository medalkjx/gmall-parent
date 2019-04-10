package com.atguigu.gmall.oms.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：mei
 * @date ：Created in 2019/4/1 0001 下午 23:59
 * @description：redisson配置
 * @modified By：
 * @version: $
 */
@Configuration
public class GmallRedissonConfig {

    @Bean
    public RedissonClient redissonClient(@Value("${spring.redis.host}") String host) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":6379");
        return Redisson.create(config);
    }
}
