package com.freedom.gpt.config;

import com.freedom.gpt.entity.SseGptMessage;
import com.freedom.gpt.utils.JsonUtil;
import com.freedom.gpt.utils.SseUtilOne;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 开启守护线程，消费BlockQueue中的消息
 * @author bk
 */
@Component
public class SseMessageCustomer implements DisposableBean,Runnable {

    private final BlockingQueue<SseGptMessage> queue = new LinkedBlockingQueue<>(100);
    private volatile boolean isRunning = true;

    @Autowired
    public SseMessageCustomer(ThreadPoolTaskExecutor gptThreadTaskExecutor) {
        Thread currThread = gptThreadTaskExecutor.createThread(this);
        currThread.start();
    }

    @Override
    public void run() {
        System.out.println("守护线程启动");
        try {
            while (isRunning) {
                SseGptMessage sseGptMessage = queue.take();
                SseEmitter emitter = SseUtilOne.INSTANCE();
                SseEmitter.SseEventBuilder message = SseEmitter.event()
                        .id(UUID.randomUUID().toString())
                        .data(JsonUtil.writeValueAsString(sseGptMessage))
                        .name("message")
                        .reconnectTime(1000L);
                emitter.send(message);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("守护线程退出");
        }
    }

    @Override
    public void destroy() throws Exception {
        isRunning = false;
        queue.clear();
        System.out.println("守护线程销毁");
    }

    public BlockingQueue<SseGptMessage> getQueue() {
        return queue;
    }
}
