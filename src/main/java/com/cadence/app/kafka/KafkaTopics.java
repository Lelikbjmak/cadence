package com.cadence.app.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("app.kafka.topic")
public class KafkaTopics {
  
  private String toCheckRequest;
  private String toCheckResponse;
}
