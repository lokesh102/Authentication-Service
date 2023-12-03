package com.AuthenticationService.AuthenticationService.repositories;

import com.AuthenticationService.AuthenticationService.models.Session;
import com.AuthenticationService.AuthenticationService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session,Long> {
    public Optional<Session> findByTokenAndUser(String token, User user);
}
