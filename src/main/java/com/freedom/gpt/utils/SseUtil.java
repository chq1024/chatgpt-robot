package com.freedom.gpt.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bk
 */
@Slf4j
@Deprecated
public class SseUtil {

    private static final ConcurrentHashMap<String,SseEmitter> emitters = new ConcurrentHashMap<>();

    private static final long defaultTimeOut = 10000L;

    public static ConcurrentHashMap<String,SseEmitter> INSTANCE() {
        return emitters;
    }

    public static SseEmitter get(String token) {
        return emitters.getOrDefault(token,null);
    }

    public static SseEmitter create(String token) {
        return create(token,defaultTimeOut);
    }

    public static SseEmitter create(String token,Long timeout) {
        SseEmitter sseEmitter = new SseEmitter(timeout);
        sseEmitter.onCompletion(()->{
            log.warn("本次结束");
            emitters.remove(token);
        });
        sseEmitter.onTimeout(()->{
            log.warn("超时");
            sseEmitter.complete();
        });
        sseEmitter.onError((e)->{
            log.error("SSE错误:{}",e.getMessage());
            sseEmitter.complete();
            emitters.remove(token);
        });
        return  sseEmitter;
    }



}
