package clowoodive.pilot.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Arrays;

@SpringBootApplication
public class TemplateApplication {

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
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

}
