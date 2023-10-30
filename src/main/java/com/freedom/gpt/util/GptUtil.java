package com.freedom.gpt.util;

import com.freedom.gpt.entity.GptMessage;
import com.freedom.gpt.entity.GptRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author bk
 */
@Component
public class GptUtil {

    @Value("${gpt_model}")
    private String gptModel;

    public static GptUtil INSTANCE = new GptUtil();

    public String genderContentKey() {
        return UUID.randomUUID().toString().replace("-","");
    }

    public List<GptMessage> newContent() {
        String initCmd = "";
        List<GptMessage> arr = new ArrayList<>(6);
        arr.add(GptMessage.builder().role("system").content("initCmd").build());
        return arr;
    }

    public GptRequest requestBody(List<GptMessage> messages) {
        return GptRequest.builder().model(gptModel).messages(messages).build();
    }
}
