package com.freedom.gpt.service;

import com.freedom.gpt.entity.GptRequest;
import com.freedom.gpt.entity.GptResponse;
import com.freedom.gpt.util.GptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bk
 */
@Service
public class GptServiceImpl implements GptService {

    @Autowired
    @Qualifier("okHttpTemplate")
    private RestTemplate restTemplate;

    @Value("${gpt_uri}")
    private String gptUri;

    private ConcurrentHashMap<String,Object> messageMap = new ConcurrentHashMap<>(16);

    public void connect(String ck,String content) {
        boolean exit = messageMap.containsKey(ck);
        if (exit) {
            //-
        }

        GptRequest gptRequest = GptUtil.INSTANCE.requestBody();

        ResponseEntity<GptResponse> response = restTemplate.postForEntity(gptUri, gptRequest, GptResponse.class);
        GptResponse body = response.getBody();

    }
}
