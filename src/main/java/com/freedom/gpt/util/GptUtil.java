package com.freedom.gpt.util;

import com.freedom.gpt.entity.GptMessage;
import com.freedom.gpt.entity.GptRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author bk
 */
@Component
public class GptUtil {

    @Value("${gpt_model}")
    private String gptModel;

    public static GptUtil INSTANCE = new GptUtil();

    public GptRequest requestBody(List<GptMessage> messages) {
        return GptRequest.builder().model(gptModel).messages(messages).build();
    }
}
