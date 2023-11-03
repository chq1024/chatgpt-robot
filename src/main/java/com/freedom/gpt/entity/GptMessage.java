package com.freedom.gpt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author bk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GptMessage implements Serializable {
    private String role;
    private String content;
}
