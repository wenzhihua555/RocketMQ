package com.duan.rocketmqdemo.rocket;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class MQCustomerCopy {
    public static void main(String[] args) {
        try {
            // 实例化消息生产者,指定组名
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("message_test1");
            // 指定Namesrv地址信息.
            consumer.setNamesrvAddr("192.168.10.41:9876");
            // 订阅Topic
            consumer.subscribe("TopicMsgTest", "*");
            //负载均衡模式消费
            consumer.setMessageModel(MessageModel.BROADCASTING);
            // 注册回调函数，处理消息
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
                System.out.printf("%s Receive New Messages: %s %n",
                        Thread.currentThread().getName(), Arrays.toString(msgs.get(0).getBody()));
                for (MessageExt msg : msgs) {
                    try {
                        System.out.println("msg内容为："+new String(msg.getBody(), RemotingHelper.DEFAULT_CHARSET));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            //启动消息者
            consumer.start();
            System.out.printf("Consumer Started.%n");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
