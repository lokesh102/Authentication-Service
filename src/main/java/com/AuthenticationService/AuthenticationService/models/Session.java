package com.AuthenticationService.AuthenticationService.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.mapping.ToOne;

import java.util.Date;

@Entity(name = "sessions")
@Getter
@Setter
public class Session extends BaseClass{
    private String token;
    private String ipAddress;
    @ManyToOne
    private User user;
    private Date expiry;
    @Enumerated(EnumType.ORDINAL)
    private SessionStatus sessionStatus;

}
