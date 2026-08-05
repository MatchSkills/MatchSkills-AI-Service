package com.matchskills.ia.service.services;

import com.matchskills.ia.service.dtos.ExtractHardskillsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.net.MalformedURLException;

@Service
public class AIService {

    private final ChatClient chatClient;
    private final String prompt;

    public AIService(ChatClient.Builder builder, @Value("${EXTRACT_HARDSKILLS_PROMPT}") String prompt) {
        this.chatClient = builder.build();
        this.prompt = prompt;
    }

    public ExtractHardskillsResponse extractHardskills(String curriculumUrl) throws MalformedURLException {

        Resource resource = new UrlResource(curriculumUrl);

        return chatClient.prompt()
                .user(u -> u
                        .text(prompt)
                        .media(MimeTypeUtils.parseMimeType("application/pdf"), resource))
                .call()
                .entity(ExtractHardskillsResponse.class);

    }

    public String test(){
        return chatClient.prompt("oi")
                .call()
                .content();
    }

}
