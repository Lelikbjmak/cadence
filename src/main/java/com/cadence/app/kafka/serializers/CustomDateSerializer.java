package com.cadence.app.kafka.serializers;

import com.cadence.app.config.JsonConfig;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.lang.Nullable;

public class CustomDateSerializer<T> extends JsonSerializer<T> {

  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

  public CustomDateSerializer() {
    super(JsonConfig.jsonMapper());
  }

  @Override
  public byte[] serialize(String topic, Headers headers, @Nullable T data) {
    if (headers != null) {
      headers.remove("nativeHeaders");
    }
    if (data == null) {
      return null;
    } else {
      if (this.addTypeInfo && headers != null) {
        this.typeMapper.fromJavaType(this.objectMapper.constructType(data.getClass()), headers);
      }

      return this.serialize(topic, data);
    }
  }
}
