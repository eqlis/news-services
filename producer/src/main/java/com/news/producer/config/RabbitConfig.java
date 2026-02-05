package com.news.producer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class RabbitConfig {
  @Value("${rabbit.news.topic-exchange}")
  private String topicExchangeName;
  @Value("${rabbit.news.queue}")
  private String queueName;
  @Value("${rabbit.news.routing-key}")
  private String routingKey;

  @Bean
  public Queue newsQueue() {
    return new Queue(queueName, false);
  }

  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(topicExchangeName);
  }

  @Bean
  public Binding binding(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(routingKey);
  }

  @Bean
  public MessageConverter messageConverter(ObjectMapper objectMapper) {
    return new Jackson2JsonMessageConverter(objectMapper);
  }

}
