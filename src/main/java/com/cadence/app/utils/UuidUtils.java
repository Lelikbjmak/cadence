package com.cadence.app.utils;

import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

@Slf4j
public class UuidUtils {

  public static Optional<UUID> tryParse(String requestId) {
    try {
      return Optional.of(UUID.fromString(requestId));
    } catch (IllegalArgumentException ex) {
      log.trace("Unknown uuid string", ex);
      return Optional.empty();
    }
  }

  public static UUID toUuid(@Nullable String string) {
    return Optional.ofNullable(string).map(UUID::fromString).orElse(null);
  }
}
