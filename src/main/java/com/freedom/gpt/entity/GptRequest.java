package com.freedom.gpt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author bk
 */
@Data
@AllArgsConstructor
@Builder
public class GptRequest implements Serializable{

    private String model;
    private List<GptMessage> messages;

}
