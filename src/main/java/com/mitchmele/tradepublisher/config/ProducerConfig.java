package com.mitchmele.tradepublisher.config;

import com.mitchmele.tradepublisher.services.StockEntityHeaderEnricher;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.amqp.support.DefaultAmqpHeaderMapper;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Configuration
@IntegrationComponentScan
public class ProducerConfig {

    @Autowired
    ConnectionFactory rabbitConnectionFactory;

    @Value("${trade-loader.rabbitmq.exchange}")
    String exchange;

    @Value("${trade-loader.rabbitmq.single-stock-exchange}")
    String singleStockExchange;

    @Value("${trade-loader.rabbitmq.queue}")
    String queueName;

    @Value("${trade-loader.rabbitmq.routingKey}")
    String routingKey;


    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange stockExchange() {
        return new TopicExchange(exchange, true, false);
    }


    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    IntegrationFlow toSingleStockFlow() {
        return IntegrationFlows.from("single-stock-channel")
                .transform(Transformers.toJson())
                .enrichHeaders(headerEnricherSpec -> headerEnricherSpec.header("Type", "STOCK"))
                .handle(Amqp.outboundAdapter(stocksRabbitTemplate())
                        .headerMapper(headerMapper())
                        .headersMappedLast(true)
                        .mappedRequestHeaders("*")
                )
                .get();
    }

    @Bean
    IntegrationFlow uploadBids(
            StockEntityHeaderEnricher stockEntityHeaderEnricher
    ) {
        return IntegrationFlows.from(
                Files.inboundAdapter(
                        new File("./BOOT-INF/classes/static"))
                        .patternFilter("*.json"), e -> e.poller(Pollers.fixedDelay(1000)))
                .split(Files.splitter())
                .log(LoggingHandler.Level.INFO, p -> "Handling new BID: " + p.getPayload())
                .enrichHeaders(headerEnricherSpec -> headerEnricherSpec.header("Type", "BID"))
                .handle(stockEntityHeaderEnricher)
                .handle(Amqp.outboundAdapter(stocksRabbitTemplate())
                        .headerMapper(headerMapper())
                        .headersMappedLast(true)
                        .mappedRequestHeaders("*")
                )
                .get();
    }


    @Bean
    IntegrationFlow uploadAsks(
            StockEntityHeaderEnricher stockEntityHeaderEnricher
    ) {
        return IntegrationFlows.from(
                Files.inboundAdapter(
                        new File("./BOOT-INF/classes/asks"))
                        .patternFilter("*.json"), e -> e.poller(Pollers.fixedDelay(1000)))
                .split(Files.splitter())
                .log(LoggingHandler.Level.INFO, p -> "Handling new ASK: " + p.getPayload())
                .enrichHeaders(headerEnricherSpec -> headerEnricherSpec.header("Type", "ASK"))
                .handle(stockEntityHeaderEnricher)
                .handle(Amqp.outboundAdapter(stocksRabbitTemplate())
                        .headerMapper(headerMapper())
                        .headersMappedLast(true)
                        .mappedRequestHeaders("*")
                )
                .get();
    }

    @Bean
    public DefaultAmqpHeaderMapper headerMapper() {
        DefaultAmqpHeaderMapper headerMapper = DefaultAmqpHeaderMapper.outboundMapper();
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put(AmqpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        headerMap.put(AmqpHeaders.DELIVERY_MODE, MessageDeliveryMode.PERSISTENT);
        headerMap.put(AmqpHeaders.CONTENT_ENCODING, "UTF-8");
        headerMap.put(AmqpHeaders.MESSAGE_ID, "BOOOOOOOO");
        headerMap.put(AmqpHeaders.TIMESTAMP, LocalDateTime.now());
        MessageHeaders integrationHeaders = new MessageHeaders(headerMap);
        MessageProperties amqpProperties = new MessageProperties();
        headerMapper.fromHeadersToRequest(integrationHeaders, amqpProperties);
        headerMapper.setRequestHeaderNames("*");
        headerMapper.setReplyHeaderNames("*");
        return headerMapper;
    }

    @Bean
    RabbitTemplate stocksRabbitTemplate() {
        RabbitTemplate r = new RabbitTemplate(rabbitConnectionFactory);
        r.setExchange(exchange);
        r.setConnectionFactory(rabbitConnectionFactory);
        return r;
    }
}
