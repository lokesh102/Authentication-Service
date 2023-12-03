package com.AuthenticationService.AuthenticationService.DTOs;

import com.AuthenticationService.AuthenticationService.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ValidateResponseDTO {
    private Long userId;
    private String email;
    private List<Role> roles;
}
