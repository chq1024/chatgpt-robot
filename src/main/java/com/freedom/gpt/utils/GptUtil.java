package com.freedom.gpt.utils;

import com.freedom.gpt.entity.GptMessage;
import com.freedom.gpt.entity.GptMessagesBody;
import com.freedom.gpt.entity.GptRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author bk
 */
@Component
public class GptUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    public static GptUtil INSTANCE;

    @Value("${gpt.key}")
    private String key;

    @Value("${gpt.model}")
    private String md;

    public static GptUtil INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = context.getBean(GptUtil.class);
        }
        return INSTANCE;
    }

    public String genderContentKey() {
        return UUID.randomUUID().toString().replace("-","");
    }

    public GptMessagesBody newContent() {
        //你现在的名字是beikei,身份是一位精通spring系列的程序员，如果问到你的知识截至之后的问题，你直接回答“我不知道”,其他的不用回答。下面开启我们的对话吧!请用简短的语言回答问题。
        String initCmd = "Your current name is beikei, and your identity is a programmer who is proficient in the spring series. If you are asked a question about your current knowledge, you can directly answer \"I don't know\", and there is no need to answer other questions. Let’s start our conversation! Please answer the questions in short language and please answer in Chinese.";
        List<GptMessage> arr = new ArrayList<>(6);
        arr.add(GptMessage.builder().role("system").content(initCmd).build());
        return GptMessagesBody.builder().chatTime(LocalDateTime.now()).messages(arr).build();
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
        GptUtil.context = context;
    }
}
