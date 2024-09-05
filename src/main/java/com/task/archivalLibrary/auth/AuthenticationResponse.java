package com.task.archivalLibrary.auth;


import com.task.archivalLibrary.DTO.UserDTO;
import com.task.archivalLibrary.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    public String token;
    public UserDTO userDTO;

}
