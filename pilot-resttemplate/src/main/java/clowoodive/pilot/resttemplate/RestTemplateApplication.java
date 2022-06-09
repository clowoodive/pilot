package clowoodive.pilot.resttemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.time.*;

@SpringBootApplication
public class RestTemplateApplication {

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    public static void main(String[] args) {
        SpringApplication.run(RestTemplateApplication.class, args);
    }

    // 최초 RestTemplete 테스트
//    @Bean
    public ApplicationRunner restTemplateRunner() {
        return args -> {
//			RestTemplate restTemplate = restTemplateBuilder.build();
            RestTemplate restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(5)).setReadTimeout(Duration.ofSeconds(5)).build();
            String result = restTemplate.getForObject("https://api.github.com/users/clowoodive/repos", String.class);
            System.out.println(result);

//			RestTemplate restTemplate = restTemplateBuilder.build();
//			GithubRepository[] results = restTemplate.getForObject("https://api.github.com/users/clowoodive/repos", GithubRepository[].class);
//			Arrays.stream(results).forEach(r -> {
//				System.out.println("repo name:" + r.getName());
//			});

        };
    }

    // Jackson2ObjectMapperBuilder 설정과 json의 OffsetDateTime을 deserialize 하는 테스트
    @Bean
    public ApplicationRunner deserializeRunner(ObjectMapper objectMapper) {
        return args -> {
            var domain = "secret";
            var clientId = "secret";

            var zonedExpireAt = ZonedDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(11, 22, 33), ZoneOffset.ofHours(9));

            System.out.println(zonedExpireAt);

            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            var reqBodyProto = new RestTemplateProto.Req();
            reqBodyProto.setClientId(clientId);
            reqBodyProto.setUserIdentifier(String.valueOf(123456));
            reqBodyProto.setIssueCategory("test");
            reqBodyProto.setExpireAt(zonedExpireAt);

            var body = objectMapper.writeValueAsString(reqBodyProto);

            var restTemplate = new RestTemplate();
            var res = restTemplate.exchange(domain, HttpMethod.POST, new HttpEntity<String>(body, headers), RestTemplateProto.Res.class);
            System.out.println(res.getBody().getExpireAt());
        };
    }

}
