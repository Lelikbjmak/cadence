package com.cadence.app.cadence.activity;

import com.cadence.app.cadence.domain.enumeration.ActivityType;
import com.cadence.app.cadence.service.ActivityContextService;
import com.cadence.app.kafka.KafkaTopics;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uber.cadence.activity.Activity;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class OsagoActivity {

  @Setter(onMethod = @__({@Autowired}))
  protected KafkaTopics kafkaTopics;
  @Setter(onMethod = @__({@Autowired}))
  protected ActivityContextService activityContextService;
  @Setter(onMethod = @__({@Autowired}))
  private ObjectMapper mapper;

  protected UUID generateAndSaveUUID(ActivityType type) {
    UUID uuid = UUID.randomUUID();
    return saveUUID(type, uuid);
  }

  protected UUID saveUUID(ActivityType type, UUID uuid) {
    byte[] taskToken = Activity.getTaskToken();
    activityContextService.putToken(uuid, taskToken, LocalDateTime.now(),
        this.getClass().getSimpleName(), type);
    return uuid;
  }

  @SneakyThrows
  protected <T> UUID generateAndSaveUUIDFromEntity(@NonNull T entity) {
    byte[] taskToken = mapper.writeValueAsBytes(entity);
    UUID uuid = UUID.randomUUID();
    activityContextService
        .putToken(uuid, taskToken, LocalDateTime.now(), this.getClass().getSimpleName(),
            ActivityType.EXTERNAL);
    return uuid;
  }

  protected Message<Object> buildMessage(UUID uuid, String topic, Object payload) {
    return MessageBuilder
        .withPayload(payload)
        .setHeader(KafkaHeaders.CORRELATION_ID, uuid)
        .setHeader(KafkaHeaders.TOPIC, topic)
        .build();
  }

  protected Message<Object> buildMessageWithReplyTopic(UUID uuid, String topic, String replyTopic,
      Object payload) {
    return MessageBuilder
        .withPayload(payload)
        .setHeader(KafkaHeaders.CORRELATION_ID, uuid.toString())
        .setHeader(KafkaHeaders.TOPIC, topic)
        .setHeader(KafkaHeaders.REPLY_TOPIC, replyTopic)
        .build();
  }

  protected Message buildMessageWithReplyTopicAndPartition(UUID uuid, String topic,
      String replyTopic, Integer partition,
      Object payload) {
    return MessageBuilder
        .withPayload(payload)
        .setHeader(KafkaHeaders.CORRELATION_ID, uuid.toString())
        .setHeader(KafkaHeaders.PARTITION_ID, partition)
        .setHeader(KafkaHeaders.TOPIC, topic)
        .setHeader(KafkaHeaders.REPLY_TOPIC, replyTopic)
        .build();
  }
}
