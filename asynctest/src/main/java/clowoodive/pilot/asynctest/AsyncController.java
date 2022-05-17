package clowoodive.pilot.asynctest;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AsyncController {
    private final AsyncService asyncService;
    private final ThreadPoolTaskExecutor executor;

    public AsyncController(AsyncService asyncService, ThreadPoolTaskExecutor executor) {
        this.asyncService = asyncService;
        this.executor = executor;
    }

    @GetMapping("/call-async")
    public String callAsync() throws InterruptedException {
        System.out.println("- init state");
        printExecutorState();
        System.out.println("- running state");
        for (int i = 0; i < 26; i++) {
            this.asyncService.runAction(i);
            printExecutorState();
        }

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
