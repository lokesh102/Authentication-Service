package com.AuthenticationService.AuthenticationService.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "roles")
@Getter
@Setter
public class Role extends BaseClass{
    private String name;
}
