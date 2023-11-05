package com.freedom.gpt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GptMessagesBody implements Serializable {
    private LocalDateTime chatTime;
    private List<GptMessage> messages;
}
