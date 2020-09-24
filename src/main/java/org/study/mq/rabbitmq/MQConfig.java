package org.study.mq.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * create by lishengbo 2019/5/27
 */
@Configuration
public class MQConfig {

    public final static String QUEUE = "queue";
    public final static String TOP_QUEUE1 = "topic_queue1";
    public final static String TOP_QUEUE2 = "topic_queue2";
    public final static String TOPIC_EXCHANGE = "topic_exchange";
    public final static String ROUTING_KEY1 = "routing.key1";
    public final static String ROUTING_KEY2 = "routing.#"; //topic模式支持通配符 #代表0个或多个 *代表1个
    public final static String FANOUT_EXCHANGE = "fanout_exchange";
    public final static String HEADERS_EXCHANGE = "header_exchange";
    public final static String HEADER_QUEUE = "header_queue";
    public final static String SECKILL_QUEUE = "seckill_queue";

    /**
     * direct 模式 交换机 Exchange
     *
     * @param
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }
    /**
     * direct 模式 交换机 Exchange
     *
     * @param
     */
    @Bean
    public Queue queueSeckill() {
        return new Queue(SECKILL_QUEUE, true);
    }

    /**
     * topic 模式 交换机 Exchange  --> 按通配规则匹配消费
     *
     * @param
     */
    @Bean
    public Queue queue1() {
        return new Queue(TOP_QUEUE1, true);
    }

    /**
     * topic 模式 交换机 Exchange
     *
     * @param
     */
    @Bean
    public Queue queue2() {
        return new Queue(TOP_QUEUE2, true);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(queue1()).to(topicExchange()).with(ROUTING_KEY1);
    }

    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(queue2()).to(topicExchange()).with(ROUTING_KEY2);
    }

    /**
     * 广播模式   --》 同时消费
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding FanoutBinding1(){
        return BindingBuilder.bind(queue1()).to(fanoutExchange());
    }

    @Bean
    public Binding FanoutBinding2(){
        return BindingBuilder.bind(queue2()).to(fanoutExchange());
    }


    /**
     * header模式   --》 msg中的消息匹配在map时 使用
     * @return
     */
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADERS_EXCHANGE);
    }
    @Bean
    public Queue headerQUeue() {
        return new Queue(HEADER_QUEUE, true);
    }
    @Bean
    public Binding bindHeader(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("header1","value1");
        map.put("header2","value2");
        return BindingBuilder.bind(headerQUeue()).to(headersExchange()).whereAll(map).match();
    }
}
