package com.news.producer.component;

import com.news.producer.config.RabbitConfig;
import com.news.producer.scraper.NewsScraper;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

@AllArgsConstructor
@EnableScheduling
@Component
public class NewsProducer {
  private static final Logger LOGGER = Logger.getLogger(NewsProducer.class.getName());

  private final NewsScraper scraper;
  private final RabbitConfig rabbitConfig;
  private final RabbitTemplate rabbitTemplate;

  @Scheduled(fixedRateString = "${scheduler.interval}")
  public void fetchAndSendNews() throws IOException {
    scraper.fetchPosts().forEach(post -> {
        LOGGER.info("Sending: " + post);
        rabbitTemplate.convertAndSend(rabbitConfig.getTopicExchangeName(), rabbitConfig.getRoutingKey(), post);
      }
    );
  }
}
