package com.freedom.gpt.service;

import com.freedom.gpt.config.SseMessageCustomer;
import com.freedom.gpt.entity.GptMessage;
import com.freedom.gpt.entity.GptMessages;
import com.freedom.gpt.entity.GptResponse;
import com.freedom.gpt.entity.SseGptMessage;
import com.freedom.gpt.openapi.ChatResponse;
import com.freedom.gpt.utils.GptUtil;
import com.freedom.gpt.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author bk
 */
@Service
@Slf4j
public class GptServiceImpl implements GptService {

    @Autowired
    @Qualifier("okHttpTemplate")
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("gptThreadTaskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private SseMessageCustomer messageCustomer;

    @Value("${gpt.uri}")
    private String gptUri;

    private final ConcurrentHashMap<String, GptMessages> messageMap = new ConcurrentHashMap<>(16);

    @Override
    @SuppressWarnings({"all"})
    public ChatResponse connect(String ck,String content) {
        boolean exit = messageMap.containsKey(ck);
        GptMessages gptMessages;
        String rck = new String(ck);
        if (StringUtils.hasText(rck) && exit) {
            gptMessages =  messageMap.get(rck);
            gptMessages.getMessages().addLast(GptMessage.builder().role("user").content(content).build());
        } else {
            // 新建会话返回初始原文和新CK
            gptMessages = GptUtil.INSTANCE().newContent();
            rck = GptUtil.INSTANCE().genderContentKey();
            if (!StringUtils.hasText(content)) {
                messageMap.put(rck,gptMessages);
                return ChatResponse.builder().ck(rck).content(gptMessages.getMessages()).chatTime(gptMessages.getChatTime()).build();
            }
            gptMessages.getMessages().add(GptMessage.builder().role("user").content(content).build());
            messageMap.put(rck,gptMessages);
        }
        try {
            System.out.println(System.currentTimeMillis());
            CompletableFuture<GptResponse> hashMapCompletableFuture = CompletableFuture.supplyAsync(() -> {
                Thread thread = Thread.currentThread();
                System.out.println(thread.getName());
                try {
                    thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // 每次GPT回复
//                String json = "{\"id\":\"chatcmpl-8FKZ4W6Tm0yU1mGAbovT6EJSbs8oK\",\"object\":\"chat.completion\",\"created\":1698664662,\"model\":\"gpt-3.5-turbo-0613\",\"choices\":[{\"index\":0,\"message\":{\"role\":\"assistant\",\"content\":\"I am the GPT-3 model developed by OpenAI. I'm a language-based model that can generate responses to prompts, answer questions, and perform various language-related tasks.\"},\"finish_reason\":\"stop\"}],\"usage\":{\"prompt_tokens\":20,\"completion_tokens\":36,\"total_tokens\":56}}";
                String json = "{\"id\":\"chatcmpl-8FKZ4W6Tm0yU1mGAbovT6EJSbs8oK\",\"object\":\"chat.completion\",\"created\":1698664662,\"model\":\"gpt-3.5-turbo-0613\",\"choices\":[{\"index\":0,\"message\":{\"role\":\"assistant\",\"content\":\""+UUID.randomUUID().toString()+"\"},\"finish_reason\":\"stop\"}],\"usage\":{\"prompt_tokens\":20,\"completion_tokens\":36,\"total_tokens\":56}}";
                GptResponse gptResponse = JsonUtil.readValue(json, GptResponse.class);
                List<GptResponse.GptChoice> choices = gptResponse.getChoices();
                GptResponse.GptChoice gptChoice = new GptResponse.GptChoice();
                gptChoice.setIndex(1);
                gptChoice.setFinish_reason("stop");
                gptChoice.setMessage(GptMessage.builder().role("assistant").content(UUID.randomUUID().toString()).build());
                choices.add(gptChoice);
                gptResponse.setChoices(choices);
                return gptResponse;
            },taskExecutor);
            // k: 上述GptResponse e:可能产生的异常
            String finalRck = rck;
            hashMapCompletableFuture.whenComplete((k, e)->{
                try {
                    GptMessage newMessage = k.getChoices().get(0).getMessage();
                    // 将newMessage加入本次历史会话
                    gptMessages.getMessages().add(newMessage);
                    messageMap.put(finalRck,gptMessages);
                    // 发送至队列
                    SseGptMessage sseGptMessage = SseGptMessage.builder().ck(finalRck).role(newMessage.getRole()).content(newMessage.getContent()).build();
                    messageCustomer.getQueue().put(sseGptMessage);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
//            HashMap map = hashMapCompletableFuture.get();
//            HashMap hashMap = hashMapCompletableFuture2.get();
            System.out.println(System.currentTimeMillis());
//            Map map = new HashMap();
//            Map map = restTemplate.postForObject(gptUri, GptUtil.INSTANCE.httpEntity(gptMessages), Map.class);
//            if (map.containsKey("error")) {
//                throw new RuntimeException(JsonUtil.writeValueAsString(map.get("error")));
//            }
//            String json = "{\"id\":\"chatcmpl-8FKZ4W6Tm0yU1mGAbovT6EJSbs8oK\",\"object\":\"chat.completion\",\"created\":1698664662,\"model\":\"gpt-3.5-turbo-0613\",\"choices\":[{\"index\":0,\"message\":{\"role\":\"assistant\",\"content\":\"I am the GPT-3 model developed by OpenAI. I'm a language-based model that can generate responses to prompts, answer questions, and perform various language-related tasks.\"},\"finish_reason\":\"stop\"}],\"usage\":{\"prompt_tokens\":20,\"completion_tokens\":36,\"total_tokens\":56}}";
//            GptResponse gptResponse = JsonUtil.readValue(json, GptResponse.class);
//            GptResponse gptResponse = JsonUtil.readValue(JsonUtil.writeValueAsString(map), GptResponse.class);
//            GptResponse.GptChoice gptChoice = gptResponse.getChoices().get(0);
//            String finish_reason = gptChoice.getFinish_reason();
//            if (!"stop".equals(finish_reason)) {
//                log.warn("gpt response finish_reason:" + finish_reason);
//                throw new RuntimeException("gpt response not you want!!!");
//            }
//            gptMessages.add(gptChoice.getMessage());
        } catch (Exception e) {
            log.error("gpt request error:{}",e.getMessage());
            throw new RuntimeException("gpt request error!!!");
        }
//        messageMap.put(rck,gptMessages);
        return ChatResponse.builder().ck(rck).content(gptMessages.getMessages()).chatTime(gptMessages.getChatTime()).build();
    }

    @Override
    public void close(String ck) {
        messageMap.remove(ck);
    }

    @Override
    public List<ChatResponse> chats() {
        return messageMap.entrySet().stream()
                .sorted(Comparator.comparing(e->e.getValue().getChatTime(),Comparator.reverseOrder()))
                .map(r-> ChatResponse.builder().ck(r.getKey()).content(r.getValue().getMessages()).chatTime(r.getValue().getChatTime()).build())
                .collect(Collectors.toList());
    }
}
