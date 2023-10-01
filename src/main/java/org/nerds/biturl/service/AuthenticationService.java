package org.nerds.biturl.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nerds.biturl.dto.AuthenticationRequest;
import org.nerds.biturl.dto.AuthenticationResponse;
import org.nerds.biturl.dto.RegisterRequest;
import org.nerds.biturl.expection.InternalServerError;
import org.nerds.biturl.expection.UserAlreadyExistException;
import org.nerds.biturl.model.Role;
import org.nerds.biturl.model.User;
import org.nerds.biturl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private final UserRepository repository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegisterRequest registerRequest) {

        Optional<User> userExist = repository.findByEmail(registerRequest.getEmail());
        if (userExist.isPresent()) throw new UserAlreadyExistException("Email already in use");

        var user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();
        try {
            repository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).build();
        } catch (Exception e) {
            throw new InternalServerError(e);
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
        );
        var user = repository.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("user name not found"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
