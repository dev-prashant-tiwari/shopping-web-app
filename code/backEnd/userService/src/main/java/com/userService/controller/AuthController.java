package com.userService.controller;

import com.userService.dto.AuthRequestDTO;
import com.userService.dto.AuthResponseDTO;
import com.userService.dto.ErrorResponse;
import com.userService.security.JwtHelper;
import com.userService.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@ControllerAdvice
public class AuthController<T> {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    AuthService authService;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtHelper jwtHelper;
    @PostMapping("/login")
    ResponseEntity<Object>login(@RequestBody AuthRequestDTO requestDTO){
        try{
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(requestDTO.getUsername(),requestDTO.getPassword());
            authenticationManager.authenticate(authenticationToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(requestDTO.getUsername());
            String token = jwtHelper.generateToken(userDetails);
            AuthResponseDTO response = AuthResponseDTO.builder().token(token).username(userDetails.getUsername()).build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (BadCredentialsException ex){
            return new ResponseEntity<>(ErrorResponse.builder().error("Invalid Credentials").build(),HttpStatus.UNAUTHORIZED);
        }
        catch (Exception ex){
            return new ResponseEntity<>(ErrorResponse.builder().error("some error occurred").build(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<T> handleRuntimeException(RuntimeException ex){
        if(ex.getMessage().equals("Invalid credentials")){
            return new ResponseEntity<T>((T) ErrorResponse.builder().error("invalid credentials"), HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity<>((T)ErrorResponse.builder().error("Access Denied"),HttpStatus.UNAUTHORIZED);
        }
    }
}
