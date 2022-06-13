package clowoodive.pilot.resttemplate.messageconverter.json.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

public abstract class RestProto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
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
        private OffsetDateTime resAt;
    }
}
