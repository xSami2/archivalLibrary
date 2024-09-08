package com.task.archivalLibrary.controller;


import com.task.archivalLibrary.DTO.AuthenticationRequest;
import com.task.archivalLibrary.DTO.AuthenticationResponse;
import com.task.archivalLibrary.service.AuthenticationService;
import com.task.archivalLibrary.DTO.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/theArchivalLibrary/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return authenticationService.registerUser(request);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        return authenticationService.authenticate(request);
    }

    @GetMapping("/authenticate")
    public ResponseEntity<String> authenticate(){
        return ResponseEntity.ok("User Authenticated");
    }



}
