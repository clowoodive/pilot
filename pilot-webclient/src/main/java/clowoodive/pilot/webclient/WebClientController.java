package clowoodive.pilot.webclient;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebClientController {
    private final WebClientService webClientService;
    private final ThreadPoolTaskExecutor executor;

    public WebClientController(WebClientService webClientService, ThreadPoolTaskExecutor executor) {
        this.webClientService = webClientService;
        this.executor = executor;
    }

    @GetMapping("/call-async")
    public String callAsync() throws InterruptedException {
        System.out.println("- init state");
        printExecutorState();
        System.out.println("- running state");
        for (int i = 0; i < 2; i++) {
            this.webClientService.runAsyncAction(i);
            Thread.sleep(5000);
            printExecutorState();
        }

        return "success";
    }

    @GetMapping("/call-solo")
    public String callSolo(){
            this.webClientService.runSoloAction();

        return "success";
    }
    private void printExecutorState() {
        System.out.print("corePoolSize: " + this.executor.getCorePoolSize());
        System.out.print(", maxPoolSize: " + this.executor.getMaxPoolSize());
        System.out.print(", poolSize: " + this.executor.getPoolSize());
        System.out.print(", queueSize: " + this.executor.getThreadPoolExecutor().getQueue().size());
        System.out.println(", activeCount: " + this.executor.getActiveCount());
    }
}
