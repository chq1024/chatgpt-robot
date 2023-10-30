package com.freedom.gpt.openapi;

import com.freedom.gpt.entity.GptMessage;
import com.freedom.gpt.service.GptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author bk
 */
@RestController
@RequestMapping("/api")
public class GptOpenController {

    @Autowired
    private GptService gptService;

    @PostMapping("/chat")
    public List<GptMessage> chat(@RequestBody ChatRequest req) {
        return gptService.connect(req.getCk(), req.getContent());
    }
}
