package org.study.mq.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * create by lishengbo 2019/5/27
 */
@Service
@Slf4j
public class MQReceiver {


    @RabbitListener(queues = MQConfig.QUEUE)
    public void receiver(String msg) {
        log.info("receive ======》"+msg);
    }

    @RabbitListener(queues = MQConfig.TOP_QUEUE1)
    public void receiverTopic1(String msg) {
        log.info("receive topic ======》"+msg);
    }

    @RabbitListener(queues = MQConfig.TOP_QUEUE2)
    public void receiverTopic2(String msg) {
        log.info("receive topic ======》"+msg);
    }
    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
    public void header(byte[] msg) {
        log.info("receive topic ======》"+new String(msg));
    }

}
