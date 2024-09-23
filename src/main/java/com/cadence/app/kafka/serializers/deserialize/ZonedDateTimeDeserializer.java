package com.cadence.app.kafka.serializers.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

  @Override
  public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException {
    String dateTime = jsonParser.getValueAsString();
    try {
      DateTimeFormatter formatter = new DateTimeFormatterBuilder()
          .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"))
          .appendOptional(DateTimeFormatter.ISO_ZONED_DATE_TIME)
          .toFormatter();

      return ZonedDateTime.parse(dateTime, formatter);
    } catch (DateTimeParseException e) {
      log.debug("Date cannot be parsed as ZonedDateTime: " + dateTime, e);
      return LocalDateTime.parse(dateTime).atZone(ZoneOffset.UTC);
    }
  }
}

