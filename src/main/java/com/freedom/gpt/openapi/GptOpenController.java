package com.freedom.gpt.openapi;

import com.freedom.gpt.service.GptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

/**
 * @author bk
 */
@RestController
@RequestMapping("/api")
public class GptOpenController {

    @Autowired
    private GptService gptService;

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest req) {
        return gptService.connect(req.getCk(), req.getContent());
    }

    @GetMapping("/chat/close")
    public void chatClose(@RequestParam("ck") @Validated @NotEmpty String ck) {
        gptService.close(ck);
    }
}
