package clowoodive.pilot.resttemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

public abstract class RestTemplateProto {

    @Setter
    public static class Req {
        @JsonProperty("client_id")
        private String clientId;

        @JsonProperty("user_identifier")
        private String userIdentifier;

        @JsonProperty("issue_category")
        private String issueCategory;

        @JsonProperty("date_of_expiry")
        private ZonedDateTime expireAt;
    }

    public static class Res {
        @JsonProperty("id")
        private String issueId;

        @JsonProperty("coupon_client_id")
        private String clientId;

        @JsonProperty("user_identifier")
        private String userIdentifier;

        @JsonProperty("issue_category")
        private String issueCategory;

        @JsonProperty("value")
        private String couponCode;

        @JsonProperty("used")
        private boolean used;

        @JsonProperty("date_of_expiry")
//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
//        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssxxx")
        @JsonDeserialize(using = JacksonConfig.CustomOffsetDateTimeDeserializer.class)
        private OffsetDateTime dateOfExpiry;

        @JsonProperty("created_by")
        private String createdBy;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;

        @JsonProperty("updated_by")
        private String updatedBy;

        @JsonProperty("updated_at")
        private LocalDateTime updatedAt;

        public OffsetDateTime getExpireAt() {
            return dateOfExpiry;
        }
    }
}
