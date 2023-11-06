package com.freedom.gpt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * @author bk
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GptMessages implements Serializable {
    private LocalDateTime chatTime;
    private LinkedList<GptMessage> messages;
}
