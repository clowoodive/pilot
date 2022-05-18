package clowoodive.pilot.webclient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@EnableAsync
@Service
public class WebClientService {
    private final WebClient webClient;

    public WebClientService() {
        HttpClient httpClient = HttpClient.create()
                .baseUrl("http://127.0.0.1:10025/")
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 500000)
                .responseTimeout(Duration.ofMillis(150000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(500000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(500000, TimeUnit.MILLISECONDS)));

        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Async
    public void runAsyncAction(int i) throws InterruptedException {
        var retVal = this.webClient.get().uri("/admin/maintenance/info")
                .retrieve().bodyToMono(String.class)
                .block();

        log.info(Thread.currentThread().getName() + " count: " + i);
        log.info(retVal);
    }

    public void runSoloAction() {
        var retVal = this.webClient.get().uri("/admin/maintenance/info")
                .retrieve().bodyToMono(String.class)
                .block();

        log.info(retVal);
    }
}
