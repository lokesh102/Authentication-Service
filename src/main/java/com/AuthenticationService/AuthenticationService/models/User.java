package com.AuthenticationService.AuthenticationService.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity(name = "users")
@Getter
@Setter
public class User extends BaseClass{
    private String email;
    private String password;
    @ManyToMany
    private List<Role> roles;

}
