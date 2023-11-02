package com.freedom.gpt.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局唯一一个emitter分发
 *
 * 后期根据需要改造成每位用户单独的SSE，根据token区分，降低消息分发压力
 * 见SseUtil
 * @author bk
 */
@Slf4j
public class SseUtilOne {

    private static SseEmitter emitter;

    private static final long DEFAULT_TIME_OUT = 10000L;

    public static SseEmitter INSTANCE() {
        if (emitter == null) {
            emitter = create();
            return emitter;
        }
        return emitter;
    }

    public static SseEmitter create() {
        return create(DEFAULT_TIME_OUT);
    }

    public static SseEmitter create(Long timeout) {
        emitter = new SseEmitter(timeout);;
        emitter.onCompletion(()->{
            log.warn("本次结束");
        });
        emitter.onTimeout(()->{
            log.warn("超时");
            if (emitter != null) {
                emitter.complete();
                emitter = null;
            }
        });
        emitter.onError((e)->{
            log.error("SSE错误:{}",e.getMessage());
            if (emitter != null) {
                emitter.complete();
                emitter = null;
            }
        });
        return  emitter;
    }



}
