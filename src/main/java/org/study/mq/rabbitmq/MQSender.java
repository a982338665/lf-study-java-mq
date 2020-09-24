package org.study.mq.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.mq.utils.Convert;

/**
 * create by lishengbo 2019/5/27
 */
@Service
@Slf4j
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;


    public void send(Object msg){
        String message= Convert.beanToString(msg);
        log.info("send ======》"+message);
        amqpTemplate.convertAndSend(MQConfig.QUEUE,message);
    }

    public void sendTopic(Object msg){
        String message= Convert.beanToString(msg);
        log.info("send topic ======》"+message);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTING_KEY1,message+"-1");
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTING_KEY2,message+"-2 ");
    }
    public void sendFanout(Object msg){
        String message= Convert.beanToString(msg);
        log.info("send fanout ======》"+message);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",message);
    }
    public void sendHeaders(Object msg){
        String message= Convert.beanToString(msg);
        log.info("send fanout ======》"+message);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("header1","value1");
        messageProperties.setHeader("header2","value2");
        Message message1 = new Message(message.getBytes(),messageProperties);
        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE,"",message1);
    }

}
