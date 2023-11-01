package com.freedom.gpt.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bk
 */
@Slf4j
public class SseUtilOne {

    private static SseEmitter emitter;

    private static final long DEFAULT_TIME_OUT = 20000L;

    public static SseEmitter INSTANCE() {
        if (emitter == null) {
            return create();
        }
        return emitter;
    }

    public static SseEmitter create() {
        return create(DEFAULT_TIME_OUT);
    }

    public static SseEmitter create(Long timeout) {
        SseEmitter sseEmitter = new SseEmitter(timeout);
        sseEmitter.onCompletion(()->{
            log.warn("本次结束");
        });
        sseEmitter.onTimeout(()->{
            log.warn("超时");
            sseEmitter.complete();
        });
        sseEmitter.onError((e)->{
            log.error("SSE错误:{}",e.getMessage());
            sseEmitter.complete();
        });
        return  sseEmitter;
    }



}
