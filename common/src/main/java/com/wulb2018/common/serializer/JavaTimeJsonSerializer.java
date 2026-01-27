package com.wulb2018.common.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author chenkaihong
 * @since 2023/3/11 10:03
 */
public class JavaTimeJsonSerializer {

    public static final JsonSerializer<LocalDateTime> LOCAL_DATE_TIME_SERIALIZER = new LocalDateTimeSerializer();
    public static final JsonDeserializer<LocalDateTime> LOCAL_DATE_TIME_DESERIALIZER = new LocalDateTimeDeserializer();


    static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime dateTime, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (dateTime != null) {
                ZoneId zone = ZoneId.systemDefault();
                Instant instant = dateTime.atZone(zone).toInstant();
                gen.writeNumber(instant.toEpochMilli());
            }
        }
    }

    static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JacksonException {
            long timestamp = parser.getValueAsLong();
            Instant instant = Instant.ofEpochMilli(timestamp);
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
    }


}
