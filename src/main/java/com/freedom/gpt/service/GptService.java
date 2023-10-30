package com.freedom.gpt.service;

import com.freedom.gpt.entity.GptMessage;

import java.util.List;

/**
 * @author bk
 */
public interface GptService {

    /**
     * 输入输出
     * @param ck 会话ID
     * @param content 输入内容
     * @return
     */
    List<GptMessage> connect(String ck, String content);
}
