package com.freedom.gpt.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bk
 */
@Data
@NoArgsConstructor
public class GptResponse implements Serializable{

    private Map<String,Object> error = new HashMap<>();
    private List<GptChoice> choices;
    private Long created;
    private String id;
    private String model;
    private String object;
    private GptOnceUsage usage;


    @Data
    @NoArgsConstructor
    public static class GptOnceUsage implements Serializable {
        private int completion_tokens;
        private int prompt_tokens;
        private int total_tokens;
    }

    @Data
    @NoArgsConstructor
    public static class GptChoice implements Serializable {
        private String finish_reason;
        private int index;
        private GptMessage message;
    }
}
