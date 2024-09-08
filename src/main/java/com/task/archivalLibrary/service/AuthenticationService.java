package com.task.archivalLibrary.service;

import com.task.archivalLibrary.DTO.UserDTO;
import com.task.archivalLibrary.DTO.AuthenticationRequest;
import com.task.archivalLibrary.DTO.AuthenticationResponse;
import com.task.archivalLibrary.DTO.RegisterRequest;
import com.task.archivalLibrary.config.JwtService;
import com.task.archivalLibrary.Enum.Role;
import com.task.archivalLibrary.entity.User;
import com.task.archivalLibrary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
   User savedUser = userRepository.save(user);
    String jwtToken = jwtService.generateToken(user);
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername(savedUser.getUsername());
    userDTO.setUserId(savedUser.getId());
    AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwtToken ,userDTO);
    return ResponseEntity.ok(authenticationResponse);
  }

  public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

      var user =
          userRepository
              .findByUsername(request.getUsername())
              .orElseThrow();
      var jwtToken = jwtService.generateToken(user);
      UserDTO userDTO = new UserDTO();
      userDTO.setUsername(user.getUsername());
      userDTO.setUserId(user.getId());
      AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwtToken , userDTO);

      return ResponseEntity.ok(authenticationResponse);

    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
