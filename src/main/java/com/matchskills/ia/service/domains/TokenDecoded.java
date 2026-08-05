package com.matchskills.ia.service.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TokenDecoded {

    private String jwtid;
    private Long userId;
    private String role;

}
