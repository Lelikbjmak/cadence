package com.cadence.app.cadence.activity.impl;

import com.cadence.app.cadence.activity.CheckRequestActivity;
import com.cadence.app.cadence.activity.OsagoActivity;
import com.cadence.app.cadence.domain.enumeration.ActivityType;
import com.cadence.app.cadence.dto.ActivityResponse;
import com.epam.edp.spring.boot.cadence.worker.annotation.Activity;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;


@Slf4j
@Activity
@RequiredArgsConstructor
public class CheckRequestActivityImpl extends OsagoActivity implements CheckRequestActivity {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Override
  public ActivityResponse<String> sendTOCheckRequest(String s) {

    UUID uuid = generateAndSaveUUID(ActivityType.TO_CHECK);
    String testPayload = UUID.randomUUID().toString();
    Message<?> message = buildMessageWithReplyTopic(uuid, kafkaTopics.getToCheckRequest(),
        kafkaTopics.getToCheckResponse(), testPayload);

    kafkaTemplate.send(message);
    log.debug("Sent request for TO check. Topic {}", kafkaTopics.getToCheckRequest());
    com.uber.cadence.activity.Activity.doNotCompleteOnReturn();
    return ActivityResponse.<String>builder().build();
  }
}
