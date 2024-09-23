package com.cadence.app.kafka.consumer;

import static com.cadence.app.utils.UuidUtils.tryParse;

import com.cadence.app.cadence.dto.ActivityResponse;
import com.cadence.app.cadence.service.ActivityContextService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TOCheckRequestResponseConsumer {

  private final ObjectMapper objectMapper;
  private final ActivityContextService activityContextService;
  private final KafkaTemplate<String, String> kafkaTemplate;

  @KafkaListener(topics = "${app.kafka.topic.toCheckResponse}")
  public void listenToCheckResponse(Message<?> message) {
    handleResponse(message);
  }

  @SneakyThrows
  private void handleResponse(Message<?> message) {
    String response = objectMapper
        .readValue((String) message.getPayload(), String.class);

    ActivityResponse<String> activityResponse = ActivityResponse.<String>builder()
        .result(response)
        .build();

    Optional<UUID> correlationId = tryParse(
        String.valueOf(message.getHeaders().get(KafkaHeaders.CORRELATION_ID)));
    if (!correlationId.isPresent()) {
      log.debug("Request id is not in form of UUID, skip it as outsider request: {}",
          message.getPayload());
      return;
    }
    log.debug("Received TO check response:\n{}", message);

    log.debug("Try to finish activity from TO CHECK QUEUE. CorrelId: {} ", correlationId);
    activityContextService.completeActivityContext(correlationId.get(), activityResponse);
    log.debug("Finished activity from TO CHECK QUEUE. CorrelId: {} ", correlationId);
  }

  @SneakyThrows
  @KafkaListener(topics = "${app.kafka.topic.toCheckRequest}")
  public void handleToCheckRequest(final Message<?> message) {
    log.info("Получен запрос 'Проверка факта прохождения ТО'");
    final String payload = (String) message.getPayload();
    final Map<String, Object> kafkaHeaders = message.getHeaders();

    Thread.sleep(5000);

    kafkaTemplate.send(new Message<Object>() {
      @Override
      public Object getPayload() {
        return payload;
      }

      @Override
      public MessageHeaders getHeaders() {
        return new MessageHeaders(kafkaHeaders);
      }
    });
    log.debug("ToCheckRequest handled, response send to Kafka");
  }
}
