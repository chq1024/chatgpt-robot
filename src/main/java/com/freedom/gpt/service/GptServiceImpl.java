package com.freedom.gpt.service;

import com.freedom.gpt.entity.GptMessage;
import com.freedom.gpt.entity.GptRequest;
import com.freedom.gpt.entity.GptResponse;
import com.freedom.gpt.util.GptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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

    @Value("${gpt_uri}")
    private String gptUri;

    private final ConcurrentHashMap<String, List<GptMessage>> messageMap = new ConcurrentHashMap<>(16);

    @Override
    public List<GptMessage> connect(String ck,String content) {
        boolean exit = messageMap.containsKey(ck);
        List<GptMessage> gptMessages;
        String rck = ck;
        if (exit) {
            gptMessages =  messageMap.get(content);
            gptMessages.add(GptMessage.builder().role("user").content(content).build());
        } else {
            gptMessages = GptUtil.INSTANCE.newContent();
            rck = GptUtil.INSTANCE.genderContentKey();
            gptMessages.add(GptMessage.builder().role("user").content(content).build());
            messageMap.put(rck,gptMessages);
        }
        GptRequest gptRequest = GptUtil.INSTANCE.requestBody(gptMessages);
        ResponseEntity<GptResponse> response = restTemplate.postForEntity(gptUri, gptRequest, GptResponse.class);
        GptResponse body = response.getBody();
        GptResponse.GptChoice gptChoice = body.getChoices().get(0);
        String finish_reason = gptChoice.getFinish_reason();
        if (!"stop".equals(finish_reason)) {
            log.warn("gpt response finish_reason:" + finish_reason);
            throw new RuntimeException("gpt response not you want!!!");
        }
        gptMessages.add(gptChoice.getMessage());
        messageMap.put(rck,gptMessages);
        return gptMessages;
    }
}
