package com.freedom.gpt.openapi;

import com.freedom.gpt.entity.GptMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author bk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponse implements Serializable {
    private String ck;
    private List<GptMessage> content;
}
