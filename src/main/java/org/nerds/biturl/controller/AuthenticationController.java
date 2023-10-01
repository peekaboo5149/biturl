package org.nerds.biturl.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.nerds.biturl.dto.AuthenticationRequest;
import org.nerds.biturl.dto.AuthenticationResponse;
import org.nerds.biturl.dto.RegisterRequest;
import org.nerds.biturl.service.AuthenticationService;
import org.nerds.biturl.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API_VERSION + "/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(
            @RequestBody @Valid RegisterRequest registerRequest
    ) {
        return ResponseEntity.ok(service.register(registerRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest authenticationRequest
    ) {
        return ResponseEntity.ok(service.authenticate(authenticationRequest));
    }
}
