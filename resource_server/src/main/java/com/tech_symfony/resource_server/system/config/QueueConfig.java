package com.tech_symfony.resource_server.system.config;

import com.tech_symfony.resource_server.system.payment.vnpay.TransactionException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
public class QueueConfig {
    @Value("${rabbitmq.queue.email.name}")
    private String emailQueue;

    @Value("${rabbitmq.exchange.email.name}")
    private String emailExchange;

    @Value("${rabbitmq.binding.email.name}")
    private String emailRoutingKey;

    @Value("${rabbitmq.queue.payment.name}")
    private String paymentQueue;

    @Value("${rabbitmq.exchange.payment.name}")
    private String paymentExchange;

    @Value("${rabbitmq.binding.payment.name}")
    private String paymentRoutingKey;

    /**
     * Dead Letter Queue (DLQ) configuration only use for payment service
     */
    @Value("${rabbitmq.queue.dlx.name}")
    private String dlxQueue;

    @Value("${rabbitmq.exchange.dlx.name}")
    private String dlxExchange;

    @Value("${rabbitmq.binding.dlx.name}")
    private String dlxRoutingKey;

    // 30 minutes
    private int messageTtl = 30 * 60 * 1000;  // TTL in milliseconds

//    private int dlxMaxTime = 30 * 60 * 1000; // Max TTL in milliseconds for messages in DLX
    private int dlxMaxTime = 3000; // Max TTL in milliseconds for messages in DLX

    private int retryDelay = 10 * 1000;      // Delay between retries (10 seconds)

    @Bean
    public Queue emailQueue() {
        return new Queue(emailQueue);
    }


    @Bean
    public TopicExchange emailExchange() {
        return new TopicExchange(emailExchange);
    }


    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(emailQueue())
                .to(emailExchange())
                .with(emailRoutingKey);
    }


    @Bean
    public Queue paymentQueue() {
        // Configuring DLX settings for payment queue
        return QueueBuilder.durable(paymentQueue)
                .withArgument("x-dead-letter-exchange", dlxExchange)
                .withArgument("x-dead-letter-routing-key", dlxRoutingKey)
                .withArgument("x-message-ttl", messageTtl)
                .build();
    }


    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(paymentExchange);
    }

    @Bean
    public Binding paymentBinding() {
        return BindingBuilder.bind(paymentQueue())
                .to(paymentExchange())
                .with(paymentRoutingKey);
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

    // DLX Configuration (Dead Letter Queue)
    @Bean
    public Queue dlxQueue() {
        // Setting maximum TTL on DLX queue to expire messages after a certain time
        return QueueBuilder.durable(dlxQueue)
                .withArgument("x-message-ttl", dlxMaxTime)  // TTL for the DLX queue
                .deadLetterExchange(paymentExchange)
                .deadLetterRoutingKey(paymentRoutingKey)
                .quorum()
                .build();
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(dlxExchange);
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue())
                .to(dlxExchange())
                .with(dlxRoutingKey);
    }

    @Bean
    public RetryOperationsInterceptor retryInterceptor() {
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(3,
                Map.of(TransactionException.class, true), true);
        return RetryInterceptorBuilder.stateless()
                .retryPolicy(retryPolicy)  // Number of retry attempts
                .backOffPolicy(exponentialBackOffPolicy()) // Exponential backoff
                .recoverer(new RejectAndDontRequeueRecoverer()) // Send to DLQ after max attempts
                .build();
    }

    @Bean
    public ExponentialBackOffPolicy exponentialBackOffPolicy() {
        ExponentialBackOffPolicy policy = new ExponentialBackOffPolicy();
        policy.setInitialInterval(1000); // Initial delay of 1 second
        policy.setMultiplier(10.0);       // Multiplies the delay each time
        policy.setMaxInterval(24 * 60 * 60 * 1000);    // Maximum delay of 24 hours
        return policy;
    }

    @Bean
    MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory,
                                                                   PlatformTransactionManager transactionManager) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setTransactionManager(transactionManager);
        container.setChannelTransacted(true);

        return container;
    }

}
