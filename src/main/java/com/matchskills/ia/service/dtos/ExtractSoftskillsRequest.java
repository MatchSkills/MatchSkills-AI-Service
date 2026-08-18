package com.matchskills.ia.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class ExtractSoftskillsRequest {

    private Long jobPostingId;
    private List<Map<String, String>> questionsAndAnswers;

}
