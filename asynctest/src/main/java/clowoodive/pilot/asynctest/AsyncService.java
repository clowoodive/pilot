package clowoodive.pilot.asynctest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Slf4j
@EnableAsync
@Service
public class AsyncService {
//    private final WebClient webClient;
//
//
//    public AsyncService(ThreadPoolTaskExecutor excutor) {
//        HttpClient httpClient = HttpClient.create()
//                .baseUrl("http://127.0.0.1:10025/")
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50000)
//                .responseTimeout(Duration.ofMillis(150000))
//                .doOnConnected(conn ->
//                        conn.addHandlerLast(new ReadTimeoutHandler(50000, TimeUnit.MILLISECONDS))
//                                .addHandlerLast(new WriteTimeoutHandler(50000, TimeUnit.MILLISECONDS)));
//
//        this.webClient = WebClient.builder()
//                .clientConnector(new ReactorClientHttpConnector(httpClient))
//                .build();
//    }

    @Async
    public void runAction(int i) throws InterruptedException {
        Thread.sleep(500);
        log.info(Thread.currentThread().getName() + " count: " + i);
    }
}
