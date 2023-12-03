package com.AuthenticationService.AuthenticationService.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateRequestDTO {
    private Long userId;
    private String token;
}
