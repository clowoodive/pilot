package clowoodive.pilot.resttemplate.messageconverter.json.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            var objectMapper = new ObjectMapper();

            String domain = "http://127.0.0.1:8080/example/offsetdatetime";

            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // custom messageconverter call
            var req = new RestTemplateProto.Req();
            req.setReqId("custom call");
            var body = objectMapper.writeValueAsString(req);

            var restTemplate = new RestTemplate();
            restTemplate.setInterceptors(Collections.singletonList(new RestInterceptor()));
            MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
            messageConverter.setObjectMapper(JacksonConfig.objectMapper());

            // 순서에 따라 문제 발생
//            restTemplate.getMessageConverters().add(1, messageConverter);

            // MappingJackson2HttpMessageConverter 2개가 등록 되는 문제
//            restTemplate.getMessageConverters().removeIf(m -> m.getClass().getName().equals(MappingJackson2HttpMessageConverter.class.getName()));
//            restTemplate.getMessageConverters().add(messageConverter);

            // 교체 방식
            for (int i = 0; i < restTemplate.getMessageConverters().size(); i++) {
                final HttpMessageConverter<?> httpMessageConverter = restTemplate.getMessageConverters().get(i);
                if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
                    restTemplate.getMessageConverters().set(i, messageConverter);
                }
            }

            var res = restTemplate.exchange(domain, HttpMethod.POST, new HttpEntity<String>(body, headers), RestTemplateProto.Res.class);
            System.out.println("resId: " + res.getBody().getResId());
            System.out.println("resAt: " + res.getBody().getResAt());

            System.out.println("");
            System.out.println("");
            System.out.println("");

            // default messageconverter call
            var defaultReq = new RestTemplateProto.Req();
            defaultReq.setReqId("default call");
            var defaultBody = objectMapper.writeValueAsString(defaultReq);

            var defaultRestTemplate = new RestTemplate();
            defaultRestTemplate.setInterceptors(Collections.singletonList(new RestInterceptor()));
            var defaultRes = defaultRestTemplate.exchange(domain, HttpMethod.POST, new HttpEntity<String>(defaultBody, headers), RestTemplateProto.Res.class);
            System.out.println("resId: " + defaultRes.getBody().getResId());
            System.out.println("resAt: " + defaultRes.getBody().getResAt());
        };
    }
}
