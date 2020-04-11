package com.duan.rocketmqdemo.clustering;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;

/**
 * 负载均衡模式消费消息
 * 即所有消费者共享一个消息源
 * @author wenzhihua
 * @date 2020/04/11
 */
public class MQCustomer {
    public static void main(String[] args) {
        try {
            // 实例化消息生产者,指定组名
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("message_test1");
            // 指定Namesrv地址信息.
            consumer.setNamesrvAddr("192.168.10.41:9876");
            // 订阅Topic
            consumer.subscribe("TopicMsgTest", "*");
            //负载均衡模式消费
            consumer.setMessageModel(MessageModel.CLUSTERING);
            // 注册回调函数，处理消息
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
                System.out.printf("%s Receive New Messages: %s %n",
                        Thread.currentThread().getName(), msgs);
                for (MessageExt msg : msgs) {
                    try {
                        System.out.println("msg内容为："+new String(msg.getBody(), RemotingHelper.DEFAULT_CHARSET));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
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
