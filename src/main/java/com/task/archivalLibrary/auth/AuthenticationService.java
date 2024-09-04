package com.task.archivalLibrary.auth;

import com.task.archivalLibrary.config.JwtService;
import com.task.archivalLibrary.entity.Role;
import com.task.archivalLibrary.entity.User;
import com.task.archivalLibrary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public ResponseEntity<AuthenticationResponse> registerUser(RegisterRequest request) {
    User user =
        User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();
    if (userRepository.existsByUsername(request.getUsername())) {
      return ResponseEntity.badRequest().build();
    }
    userRepository.save(user);
    String jwtToken = jwtService.generateToken(user);
    AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwtToken);
    return ResponseEntity.ok(authenticationResponse);
  }

  public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
    try {
      // Attempt to authenticate the user
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

      // If authentication is successful, fetch the user and generate a token
      var user =
          userRepository
              .findByUsername(request.getUsername())
              .orElseThrow(); // This should ideally be a more specific exception
      var jwtToken = jwtService.generateToken(user);
      AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwtToken);

      return ResponseEntity.ok(authenticationResponse);

    } catch (BadCredentialsException e) {
      // Handle case where credentials are invalid
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
