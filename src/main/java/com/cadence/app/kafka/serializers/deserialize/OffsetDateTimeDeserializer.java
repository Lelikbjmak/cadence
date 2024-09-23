package com.cadence.app.kafka.serializers.deserialize;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

  @Override
  public OffsetDateTime deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext)
      throws IOException {
    String dateTime = jsonParser.getValueAsString();
    try {
      //all input dates shouldn't use nano seconds
      return OffsetDateTime.parse(dateTime);
    } catch (DateTimeParseException e) {
      log.debug("OffsetDateTime does not support format. Original message: {}", e.getMessage(), e);
      return LocalDateTime.parse(dateTime).atOffset(ZoneOffset.UTC);
    }
  }
}