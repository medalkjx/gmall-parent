package com.atguigu.gmall.oms.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：mei
 * @date ：Created in 2019/4/3 0003 上午 11:52
 * @description：RabbitMQ配置
 * @modified By：
 * @version: $
 */
@Configuration
public class GmallRabbitMQConfig {

    @Bean
    public Exchange orderExchange() {
        return new FanoutExchange("orderExchange", true, false);
    }

    @Bean
    public Exchange deadExchange() {
        return new DirectExchange("deadExchange", true, false);
    }


    @Bean
    public Queue stockQueue() {
        return new Queue("stockQueue", true, false, false);
    }

    @Bean
    public Queue userQueue() {
        return new Queue("userQueue", true, false, false);
    }

    @Bean
    public Queue deadQueue() {
        return new Queue("deadQueue", true, false, false);
    }

    @Bean
    public Queue releaseStockQueue() {
        return new Queue("releaseStockQueue", true, false, false);
    }

    @Bean
    public Queue orderDelayQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "deadExchange");
        arguments.put("x-dead-letter-routing-key", "dead.order");
        arguments.put("x-message-ttl", 1000 * 60 * 30);
        return new Queue("orderDelayQueue", true, false, false, arguments);
    }


    @Bean
    public Binding stockQueueBinding() {
        return new Binding("stockQueue",
                Binding.DestinationType.QUEUE,
                "orderExchange",
                "", null);
    }

    @Bean
    public Binding userQueueBinding() {
        return new Binding("userQueue",
                Binding.DestinationType.QUEUE,
                "orderExchange",
                "", null);
    }

    @Bean
    public Binding deadQueueBinding() {
        return new Binding("deadQueue",
                Binding.DestinationType.QUEUE,
                "deadExchange",
                "dead.order", null);
    }
    @Bean
    public Binding releaseStockQueueBinding() {
        return new Binding("releaseStockQueue",
                Binding.DestinationType.QUEUE,
                "deadExchange",
                "release.stock", null);
    }

    @Bean
    public Binding orderDelayQueueBinding() {
        return new Binding("orderDelayQueue",
                Binding.DestinationType.QUEUE,
                "orderExchange",
                "", null);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
