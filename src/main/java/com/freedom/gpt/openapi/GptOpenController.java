package com.freedom.gpt.openapi;

import com.freedom.gpt.service.GptService;
import com.freedom.gpt.utils.SseUtilOne;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author bk
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class GptOpenController {


    @Autowired
    private GptService gptService;

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest req){
        return gptService.connect(req.getCk(), req.getContent());
    }

    @GetMapping("/chat/close")
    public void chatClose(@RequestParam("ck") @Validated @NotEmpty String ck) {
        gptService.close(ck);
    }

    @GetMapping("/chat")
    public List<ChatResponse> chat() {
        return gptService.chats();
    }

    @GetMapping("/sse")
    public SseEmitter handleSse() {
        return SseUtilOne.INSTANCE();
    }
}
