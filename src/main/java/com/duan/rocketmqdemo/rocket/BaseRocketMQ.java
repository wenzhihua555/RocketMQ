package com.duan.rocketmqdemo.rocket;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created on 2020/04/11.
 *
 * @author 文志华
 */
@Data
public abstract class BaseRocketMQ {

    @Value("${rocketmq.producer.group.test}")
    protected String producerGroup;

    @Value("${rocketmq.consumer.group.test}")
    protected String consumerGroup;

    @Value("${rocketmq.nameSrv.address}")
    protected String nameSrvAddr;

    public abstract void shutdown();

}
