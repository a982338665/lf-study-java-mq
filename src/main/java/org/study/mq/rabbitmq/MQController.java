package org.study.mq.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.study.mq.common.CodeMsg;
import org.study.mq.common.Result;

/**
 * @author:luofeng
 * @createTime : 2018/10/9 16:02
 */
@Controller
@RequestMapping("/mq")
public class MQController
{

    @Autowired
    MQSender mqSender;

    /**
     * http://localhost:8080/mq/send
     * @return
     */
    @RequestMapping("/send")
    @ResponseBody
    public Result<String> error(){
        mqSender.send("hello mq!");
        return Result.error(CodeMsg.SESSION_ERROR);
    }

    @RequestMapping("/sendTopic")
    @ResponseBody
    public Result<String> sendTopic(){
        mqSender.sendTopic("hello topic mq!");
        return Result.error(CodeMsg.SESSION_ERROR);
    }

    @RequestMapping("/sendFanout")
    @ResponseBody
    public Result<String> sendFanout(){
        mqSender.sendFanout("hello fanout mq!");
        return Result.error(CodeMsg.SESSION_ERROR);
    }
    @RequestMapping("/sendHeader")
    @ResponseBody
    public Result<String> sendHeader(){
        mqSender.sendHeaders("hello header mq!");
        return Result.error(CodeMsg.SESSION_ERROR);
    }

}
