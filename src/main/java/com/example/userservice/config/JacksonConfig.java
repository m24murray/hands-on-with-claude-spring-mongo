package com.example.userservice.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Jackson configuration for custom serialization.
 */
@Configuration
public class JacksonConfig {

    /**
     * Custom ObjectId serializer that converts ObjectId to hexadecimal string.
     */
    public static class ObjectIdSerializer extends JsonSerializer<ObjectId> {
        @Override
        public void serialize(ObjectId value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.toHexString());
        }
    }

    /**
     * Configure custom ObjectMapper with ObjectId serializer.
     * 
     * @return The customized ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(ObjectId.class, new ObjectIdSerializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }
}