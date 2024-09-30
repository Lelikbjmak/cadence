package com.cadence.app.cadence.service;

import static com.cadence.app.cadence.domain.enumeration.ActivityType.BSO_COMPENSATION;

import com.cadence.app.cadence.domain.ActivityContextEntity;
import com.cadence.app.cadence.domain.enumeration.ActivityType;
import com.cadence.app.cadence.repository.ActivityContextRepository;
import com.uber.cadence.client.ActivityCompletionClient;
import com.uber.cadence.client.ActivityNotExistsException;
import io.micrometer.core.instrument.MeterRegistry;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Утилитаный класс-хранилище UUID, токена Cadence Activity.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityContextService {

  private final ActivityContextRepository contextRepository;
  private final ActivityCompletionClient completionClient;

  public void putToken(UUID uuid, byte[] tokenId, LocalDateTime start, String activityName,
      ActivityType activityType) {
    ActivityContextEntity entity = ActivityContextEntity.builder()
        .activityId(uuid.toString())
        .token(new String(tokenId, StandardCharsets.UTF_8))
        .start(start)
        .activityName(activityName)
        .activityType(activityType)
        .build();
    contextRepository.save(entity);
  }

  public <T> byte[] completeActivityContext(UUID uuid, T response) {
    final Optional<ActivityContextEntity> activityEntity = Optional
        .ofNullable(contextRepository.findByActivityId(uuid.toString()));
    return activityEntity.map(
            contextEntity -> processActivityCompletion(uuid, response, contextEntity))
        .orElse(null);
  }

  private <T> byte[] processActivityCompletion(UUID uuid, T response,
      ActivityContextEntity activityContext) {

    contextRepository.delete(activityContext);

    byte[] token = Optional.ofNullable(activityContext.getToken())
        .map(t -> t.getBytes(StandardCharsets.UTF_8))
        .orElse(null);

    if (token == null) {
      return null;
    }

    Duration totalDuration = Duration.between(activityContext.getStart(), LocalDateTime.now());
    String activityName = activityContext.getActivityName();

    boolean isCompensation = BSO_COMPENSATION == activityContext.getActivityType();
    if (!isCompensation) {
      completeActivity(uuid, response, activityName, token);
    }

    log.debug("Cadence activity {} completed in {}ms",
        activityName,
        totalDuration.toMillis());

    return null;
  }

  private <T> void completeActivity(UUID uuid, T response, String activityName, byte[] token) {
    try {
      completionClient.complete(token, response);
    } catch (ActivityNotExistsException ex) {
      log.debug("Activity already stopped. UUID: " + uuid + ", " + activityName, ex);
    } catch (Exception ex) {
      log.error("Activity completion error. UUID: " + uuid + ", " + activityName, ex);
    }
  }
}
