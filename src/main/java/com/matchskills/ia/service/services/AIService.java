package com.matchskills.ia.service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchskills.ia.service.dtos.ExtractHardskillsResponse;
import com.matchskills.ia.service.dtos.ExtractSoftskillsRequest;
import com.matchskills.ia.service.dtos.ExtractSoftskillsResponse;
import com.matchskills.ia.service.entitys.AnchorEntity;
import com.matchskills.ia.service.exceptions.customs.jobposting.JobPostingNotFoundException;
import com.matchskills.ia.service.repositorys.JobPostingRepository;
import com.matchskills.ia.service.repositorys.SoftskillRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AIService {

    final private SoftskillRepository softskillRepository;
    final private ChatClient chatClient;
    final private String extractPrompt;
    final private Resource resultsPrompt;
    final private JobPostingRepository jobPostingRepository;

    public AIService(ChatClient.Builder builder,
                     @Value("${extract.hardskills.prompt.file}") String extractPrompt,
                     @Value("${get.softskills.prompt.file}") Resource resultsPrompt,
                     SoftskillRepository softskillRepository,
                     JobPostingRepository jobPostingRepository
    ) {
        this.chatClient = builder.build();
        this.resultsPrompt = resultsPrompt;
        this.extractPrompt = extractPrompt;
        this.softskillRepository = softskillRepository;
        this.jobPostingRepository = jobPostingRepository;
    }

    public ExtractHardskillsResponse extractHardskills(String curriculumUrl) throws MalformedURLException {

        Resource resource = new UrlResource(curriculumUrl);

        return chatClient.prompt()
                .user(u -> u
                        .text(extractPrompt)
                        .media(MimeTypeUtils.parseMimeType("application/pdf"), resource))
                .call()
                .entity(ExtractHardskillsResponse.class);

    }

    public ExtractSoftskillsResponse generateSoftSkillResults(
            ExtractSoftskillsRequest extractSoftskillsRequest
    ) throws IOException {

        String prompt = new String(
                resultsPrompt.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );

        var targetSoftSkills = jobPostingRepository.findById(extractSoftskillsRequest.getJobpostingId())
                .orElseThrow(JobPostingNotFoundException::new);

        var targetSoftskillsSet = targetSoftSkills.getTargetSoftskills().keySet();

        var targetSoftSkillsInfo = softskillRepository.findByNameIgnoreCaseIn(targetSoftskillsSet);

        Map<String, Object> input = Map.of(
                "softskills", targetSoftSkillsInfo.stream().map(s -> Map.of(
                        "name", s.getName(),
                        "description", s.getDescription(),
                        "bars", s.getBars().stream().collect(Collectors.toMap(
                                a -> String.valueOf(a.getLevel()), AnchorEntity::getDescription))
                )).toList(),
                "qa", extractSoftskillsRequest.getQuestionsAndAnswers()
        );

        String payload = new ObjectMapper().writeValueAsString(input);

        return chatClient.prompt(prompt + "\n\n" + payload)
                .call()
                .entity(ExtractSoftskillsResponse.class);

    }

}
