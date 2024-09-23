package com.cadence.app.kafka.serializers;

import com.cadence.app.kafka.serializers.deserialize.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.ZonedDateTime;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class CustomDateDeserializer extends JsonDeserializer<Object> {

  public CustomDateDeserializer() {
    super();
    SimpleModule simpleModule = new SimpleModule();
    simpleModule.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.registerModule(simpleModule);
  }
}
