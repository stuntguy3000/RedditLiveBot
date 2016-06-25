package me.stuntguy3000.java.redditlivebot.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Data;

/**
 * Handles the execution of multiple threads
 * <p>Used for simultaneous message posting</p>
 *
 * @author stuntguy3000
 */
@Data
public class ThreadExecutionHandler {
    private ExecutorService executorService;

    public ThreadExecutionHandler() {
        executorService = Executors.newCachedThreadPool();
    }

    public void queue(Runnable runnable) {
        executorService.submit(runnable);
    }
}
