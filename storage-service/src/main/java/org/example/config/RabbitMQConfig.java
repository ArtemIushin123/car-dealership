package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQConfig {
    public static final String ORDER_SENT_FOR_APPROVAL_QUEUE = "order.sent-for-approval-queue";
    public static final String ORDER_APPROVAL_QUEUE = "order.approval-queue";
    public static final String ORDER_REJECTED_QUEUE = "order.rejected-queue";

    @Bean
    public Queue orderSentForApprovalQueue() {
        return new Queue(ORDER_SENT_FOR_APPROVAL_QUEUE, true);
    }

    @Bean
    public Queue orderApprovalQueue() {
        return new Queue(ORDER_APPROVAL_QUEUE, true);
    }

    @Bean
    public Queue orderRejectedQueue() {
        return new Queue(ORDER_REJECTED_QUEUE, true);
    }
}
