package com.AuthenticationService.AuthenticationService.DTOs;

import com.AuthenticationService.AuthenticationService.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String email;

    public static UserDTO from(User newUser) {
        UserDTO signUpResponseDTO = new UserDTO();
        signUpResponseDTO.setEmail(newUser.getEmail());
        return signUpResponseDTO;
    }
}
