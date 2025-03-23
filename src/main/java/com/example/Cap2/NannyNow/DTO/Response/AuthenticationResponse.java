package com.example.Cap2.NannyNow.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private String jwt;
    private Long userId;
    private String roleName;
    
    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }
}

