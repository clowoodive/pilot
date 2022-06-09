package clowoodive.pilot.resttemplate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;

@Component
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
//        JavaTimeModule javaTimeModule = new JavaTimeModule();
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
                .failOnUnknownProperties(false)
                .failOnEmptyBeans(true)
//                .modules(javaTimeModule)
                ;
    }

    public static class CustumOffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {
        @Override
        public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            return OffsetDateTime.parse(
                    jsonParser.getText()
//                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx")
            );
        }
    }
}
