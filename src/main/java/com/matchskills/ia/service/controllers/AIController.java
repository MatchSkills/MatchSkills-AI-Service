package com.matchskills.ia.service.controllers;

import com.matchskills.ia.service.dtos.ExtractHardskillsRequest;
import com.matchskills.ia.service.dtos.ExtractHardskillsResponse;
import com.matchskills.ia.service.services.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {

    final private AIService service;

    @PostMapping("/extract-hardskills")
    public ExtractHardskillsResponse extractHardskills(@RequestBody ExtractHardskillsRequest extractHardskillsRequest) throws MalformedURLException {

        return service.extractHardskills(extractHardskillsRequest.getCurriculum_url());
    }

    @GetMapping
    public String test(){
        return service.test();
    }

}
