package com.freedom.gpt.service;

import com.freedom.gpt.openapi.ChatResponse;

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
    ChatResponse connect(String ck, String content);

    /**
     * 关闭会话
     * @param ck
     */
    void close(String ck);
}
