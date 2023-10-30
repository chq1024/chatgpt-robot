package com.freedom.gpt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freedom.gpt.entity.GptMessage;
import com.freedom.gpt.entity.GptResponse;
import com.freedom.gpt.utils.GptUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bk
 */
@Service
@Slf4j
public class GptServiceImpl implements GptService {

    @Autowired
    @Qualifier("okHttpTemplate")
    private RestTemplate restTemplate;

    @Value("${gpt.uri}")
    private String gptUri;

    @Value("${gpt.key}")
    private String gptKey;

    private final ConcurrentHashMap<String, List<GptMessage>> messageMap = new ConcurrentHashMap<>(16);

    @Override
    @SuppressWarnings({"all"})
    public List<GptMessage> connect(String ck,String content) {
        boolean exit = messageMap.containsKey(ck);
        List<GptMessage> gptMessages;
        String rck = ck;
        if (exit) {
            gptMessages =  messageMap.get(content);
            gptMessages.add(GptMessage.builder().role("user").content(content).build());
        } else {
            gptMessages = GptUtils.INSTANCE().newContent();
            rck = GptUtils.INSTANCE().genderContentKey();
            gptMessages.add(GptMessage.builder().role("user").content(content).build());
            messageMap.put(rck,gptMessages);
        }
        try {
//            Map map = restTemplate.postForObject(gptUri, GptUtils.INSTANCE.httpEntity(gptMessages), Map.class);
            GptResponse body = new GptResponse();
//            GptResponse body = response.getBody();
            GptUtils.INSTANCE().genderContentKey();
            String json = "{\"id\":\"chatcmpl-8FKZ4W6Tm0yU1mGAbovT6EJSbs8oK\",\"object\":\"chat.completion\",\"created\":1698664662,\"model\":\"gpt-3.5-turbo-0613\",\"choices\":[{\"index\":0,\"message\":{\"role\":\"assistant\",\"content\":\"I am the GPT-3 model developed by OpenAI. I'm a language-based model that can generate responses to prompts, answer questions, and perform various language-related tasks.\"},\"finish_reason\":\"stop\"}],\"usage\":{\"prompt_tokens\":20,\"completion_tokens\":36,\"total_tokens\":56}}";
            GptResponse gptResponse = new ObjectMapper().readValue(json, GptResponse.class);
            GptResponse.GptChoice gptChoice = gptResponse.getChoices().get(0);
            String finish_reason = gptChoice.getFinish_reason();
            if (!"stop".equals(finish_reason)) {
                log.warn("gpt response finish_reason:" + finish_reason);
                throw new RuntimeException("gpt response not you want!!!");
            }
            gptMessages.add(gptChoice.getMessage());
        } catch (Exception e) {
            log.error("gpt request error:{}",e.getMessage());
            throw new RuntimeException("gpt request error!!!");
        }
        messageMap.put(rck,gptMessages);
        return gptMessages;
    }
}
