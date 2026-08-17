package com.matchskills.ia.service.controllers;

import com.matchskills.ia.service.dtos.*;
import com.matchskills.ia.service.entitys.SoftskillEntity;
import com.matchskills.ia.service.services.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {

    final private AIService service;

    @PostMapping("/extract-hardskills")
    @PreAuthorize("hasRole('SYSTEM')")
    public ExtractHardskillsResponse extractHardskills(@RequestBody ExtractHardskillsRequest extractHardskillsRequest) throws MalformedURLException {

        return service.extractHardskills(extractHardskillsRequest.getCurriculumUrl());
    }

    @GetMapping("/extract-softskills")
    @PreAuthorize("hasRole('SYSTEM')")
    public ExtractSoftskillsResponse extractSoftSkills(@RequestBody ExtractSoftskillsRequest extractSoftskillsRequest) throws IOException {
        return service.generateSoftSkillResults(extractSoftskillsRequest);
    }

}
