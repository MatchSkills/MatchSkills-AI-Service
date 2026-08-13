package com.matchskills.ia.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class TargetSoftskillsResponse {

    private List<Map<String, Integer>> softskills;

}
