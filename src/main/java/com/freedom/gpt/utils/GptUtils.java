package com.freedom.gpt.utils;

import com.freedom.gpt.entity.GptMessage;
import com.freedom.gpt.entity.GptRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author bk
 */
@Component
public class GptUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    public static GptUtils INSTANCE;

    @Value("${gpt.key}")
    private String key;

    @Value("${gpt.model}")
    private String md;

    public static GptUtils INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = context.getBean(GptUtils.class);
        }
        return INSTANCE;
    }

    public String genderContentKey() {
        return UUID.randomUUID().toString().replace("-","");
    }

    public List<GptMessage> newContent() {
        String initCmd = "";
        List<GptMessage> arr = new ArrayList<>(6);
        arr.add(GptMessage.builder().role("system").content("initCmd").build());
        return arr;
    }

    private GptRequest requestBody(List<GptMessage> messages) {
        return GptRequest.builder().model(md).messages(messages).build();
    }

    private HttpHeaders requestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(key);
        return headers;
    }

    public HttpEntity<GptRequest> httpEntity(List<GptMessage> messages) {
        GptRequest body = requestBody(messages);
        HttpHeaders headers = requestHeaders();
        return new HttpEntity<>(body,headers);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        GptUtils.context = context;
    }
}
