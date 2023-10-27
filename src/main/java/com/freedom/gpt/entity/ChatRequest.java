package com.freedom.gpt.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author bk
 */
@Getter
@Setter
@NoArgsConstructor
public class ChatRequest implements Serializable {
    private String ck;
    private String content;
}
