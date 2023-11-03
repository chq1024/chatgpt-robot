package com.freedom.gpt.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author bk
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class SseGptMessage extends GptMessage {
    private String ck;
}
