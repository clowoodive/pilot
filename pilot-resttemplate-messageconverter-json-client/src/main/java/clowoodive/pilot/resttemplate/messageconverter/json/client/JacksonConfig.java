package clowoodive.pilot.resttemplate.messageconverter.json.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class JacksonConfig {
    //    @Bean
    public static ObjectMapper objectMapper() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(OffsetDateTime.class, new CustomOffsetDateTimeDeserializer());
        return new ObjectMapper()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true)
                .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
                .registerModules(javaTimeModule);
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
//        JavaTimeModule javaTimeModule = new JavaTimeModule();
//        javaTimeModule.addDeserializer(OffsetDateTime.class, new CustomOffsetDateTimeDeserializer());

//        javaTimeModule.addDeserializer(OffsetDateTime.class, new JsonDeserializer<OffsetDateTime>() {
//            @Override
//            public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
//                return OffsetDateTime.parse(
//                        jsonParser.getText()
////                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx")
//                );
//            }
//        });


        return new Jackson2ObjectMapperBuilder()
                .defaultViewInclusion(false)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .featuresToDisable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
//                .featuresToDisable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .failOnUnknownProperties(false)
                .failOnEmptyBeans(true)
//                .modulesToInstall(javaTimeModule)
//                .modules(javaTimeModule)
                ;
    }

    public static class CustomOffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {
        @Override
        public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            return OffsetDateTime.parse(
                    jsonParser.getText(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx") // 없어도 됨
            );
        }
    }
}
