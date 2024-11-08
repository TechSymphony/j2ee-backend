package com.tech_symfony.resource_server.system.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.transaction.PlatformTransactionManager;

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

    @Bean
    public Queue emailQueue(){
        return new Queue(emailQueue);
    }


    @Bean
    public TopicExchange emailExchange(){
        return new TopicExchange(emailExchange);
    }


    @Bean
    public Binding emailBinding(){
        return BindingBuilder.bind(emailQueue())
                .to(emailExchange())
                .with(emailRoutingKey);
    }


    @Bean
    public Queue paymentQueue(){
        return new Queue(paymentQueue);
    }


    @Bean
    public TopicExchange paymentExchange(){
        return new TopicExchange(paymentExchange);
    }


    @Bean
    public Binding paymentBinding(){
        return BindingBuilder.bind(emailQueue())
                .to(emailExchange())
                .with(paymentRoutingKey);
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
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
