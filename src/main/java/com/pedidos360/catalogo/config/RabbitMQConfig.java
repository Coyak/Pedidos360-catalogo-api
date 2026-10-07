package com.pedidos360.catalogo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuración centralizada de RabbitMQ para Pedidos360 Catálogo API.
 * Declara la infraestructura de cola de inventario (Stock Queue) y su DLQ asociada,
 * configurando confirmación manual (ACK/NACK) y deserialización JSON Jackson.
 */
@Configuration
public class RabbitMQConfig {

    @Value("${pedidos360.rabbitmq.exchange.pedidos}")
    private String pedidosExchangeName;

    @Value("${pedidos360.rabbitmq.exchange.dlx}")
    private String dlxExchangeName;

    @Value("${pedidos360.rabbitmq.queue.stock}")
    private String stockQueueName;

    @Value("${pedidos360.rabbitmq.queue.stock-dlq}")
    private String stockDlqName;

    @Value("${pedidos360.rabbitmq.routing-key.creado}")
    private String pedidoCreadoRoutingKey;

    @Value("${pedidos360.rabbitmq.routing-key.stock-dlq}")
    private String stockDlqRoutingKey;

    // 1. Topic Exchange Principal de Pedidos
    @Bean
    public TopicExchange pedidosExchange() {
        return new TopicExchange(pedidosExchangeName, true, false);
    }

    // 2. Direct Exchange para Dead Letter Queue
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(dlxExchangeName, true, false);
    }

    // 3. Cola de Descuento de Stock con configuración Dead Letter Exchange
    @Bean
    public Queue stockQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", dlxExchangeName);
        args.put("x-dead-letter-routing-key", stockDlqRoutingKey);

        return QueueBuilder.durable(stockQueueName)
                .withArguments(args)
                .build();
    }

    // 4. Cola DLQ para Stock con mensajes fallidos
    @Bean
    public Queue stockDlq() {
        return QueueBuilder.durable(stockDlqName).build();
    }

    // 5. Binding entre TopicExchange y Cola de Stock
    @Bean
    public Binding bindingStock() {
        return BindingBuilder
                .bind(stockQueue())
                .to(pedidosExchange())
                .with(pedidoCreadoRoutingKey);
    }

    // 6. Binding entre Dead Letter Exchange y Cola DLQ de Stock
    @Bean
    public Binding bindingStockDlq() {
        return BindingBuilder
                .bind(stockDlq())
                .to(deadLetterExchange())
                .with(stockDlqRoutingKey);
    }

    // 7. Conversor de mensajes JSON con módulo de fechas JavaTime
    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    // 8. Contenedor de listeners con soporte para confirmación MANUAL (ACK/NACK explícito)
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setPrefetchCount(10);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}
