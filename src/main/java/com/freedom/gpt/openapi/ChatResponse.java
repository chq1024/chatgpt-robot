package com.freedom.gpt.openapi;

import com.freedom.gpt.entity.GptMessagesBody;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author bk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponse implements Serializable {
    private String ck;
    private GptMessagesBody content;
}
