package clowoodive.pilot.resttemplate.messageconverter.json.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

public abstract class RestTemplateProto {

    @Getter
    @Setter
    public static class Req {
        @JsonProperty("req_id")
        private String reqId;
    }

    @Getter
    @Setter
    public static class Res {
        @JsonProperty("res_id")
        private String resId;

        @JsonProperty("res_at")
//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssxxx")
//        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssxxx")
//        @JsonDeserialize(using = JacksonConfig.CustomOffsetDateTimeDeserializer.class)
        private OffsetDateTime resAt;
    }
}
