package com.matchskills.ia.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ExtractHardskillsResponse {

    private List<String> hardskills;

}
