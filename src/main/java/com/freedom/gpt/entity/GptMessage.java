package com.freedom.gpt.entity;

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
public class GptMessage implements Serializable {
    private String role;
    private String content;
}
