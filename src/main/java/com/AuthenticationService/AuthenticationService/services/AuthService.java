package com.AuthenticationService.AuthenticationService.services;

import com.AuthenticationService.AuthenticationService.DTOs.LogOutResponseDTO;
import com.AuthenticationService.AuthenticationService.DTOs.LoginResponseDTO;
import com.AuthenticationService.AuthenticationService.DTOs.UserDTO;
import com.AuthenticationService.AuthenticationService.DTOs.ValidateResponseDTO;
import com.AuthenticationService.AuthenticationService.exceptions.EmailAlreadyExistsException;
import com.AuthenticationService.AuthenticationService.exceptions.UserNotFoundException;
import com.AuthenticationService.AuthenticationService.models.Session;
import com.AuthenticationService.AuthenticationService.models.SessionStatus;
import com.AuthenticationService.AuthenticationService.models.User;
import com.AuthenticationService.AuthenticationService.repositories.SessionRepository;
import com.AuthenticationService.AuthenticationService.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.jmx.export.notification.UnableToSendNotificationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {
    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    public AuthService(UserRepository userRepository,SessionRepository sessionRepository){
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }
    public Optional<UserDTO> signUp(String email, String password) throws EmailAlreadyExistsException {
        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isEmpty()){
            throw new EmailAlreadyExistsException(email+" already exists");
        }
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(password);
        userRepository.save(newUser);

        return Optional.of(UserDTO.from(newUser));
    }
    public Optional<LoginResponseDTO> login(String email, String password) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException(email +" no user with this email");
        }
        User user = userOptional.get();
        if(user.getEmail().equals(email) && user.getPassword().equals(password)){
            //String token = RandomStringUtils.randomAscii(20);
            String token1 = RandomStringUtils.randomAlphanumeric(20);
            Session session = new Session();
            session.setUser(user);
            session.setToken(token1);
            session.setSessionStatus(SessionStatus.ACTIVE);
            sessionRepository.save(session);
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setUserId(user.getId());
            loginResponseDTO.setEmail(user.getEmail());
            loginResponseDTO.setToken(session.getToken());
            return Optional.of(loginResponseDTO);
        }
            throw new UserNotFoundException(email +" no user found");
        //return null;
    }
    public LogOutResponseDTO logout(String token, Long userId){
        LogOutResponseDTO logOutResponseDTO = new LogOutResponseDTO();
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            logOutResponseDTO.setStatus("user not available");
        }
        User user1 = user.get();
        Optional<Session> session = sessionRepository.findByTokenAndUser(token,user1);
        if(session.isPresent()){
            Session currentSession = session.get();
            currentSession.setSessionStatus(SessionStatus.EXPIRED);
            sessionRepository.save(currentSession);
            logOutResponseDTO.setStatus("user id "+userId+" logged out.");
            return logOutResponseDTO;
        }
        logOutResponseDTO.setStatus("user not found");
        return logOutResponseDTO;

    }
    public ValidateResponseDTO validate(String token,Long userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException("user not found");
        }
        Optional<Session> session = sessionRepository.findByTokenAndUser(token,user.get());
        if(session.isPresent()){
            Session session1 = session.get();
            if(session1.getSessionStatus() == SessionStatus.EXPIRED){
                throw new UserNotFoundException("user not found");
            }
            Date tokenDate = session1.getExpiry();
            Date currDate = Date.from(Instant.now());
            if(tokenDate != null && tokenDate.before(currDate)){
                session1.setSessionStatus(SessionStatus.EXPIRED);
                sessionRepository.save(session1);
                throw new UserNotFoundException("user not found");
            }
            ValidateResponseDTO validateResponseDTO = new ValidateResponseDTO();
            validateResponseDTO.setRoles(user.get().getRoles());
            validateResponseDTO.setEmail(user.get().getEmail());
            validateResponseDTO.setUserId(userId);
            return validateResponseDTO;
        }
        throw new UserNotFoundException("user not found");
    }
}
