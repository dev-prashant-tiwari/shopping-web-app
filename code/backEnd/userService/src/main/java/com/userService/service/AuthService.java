package com.userService.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public ResponseEntity<Object>doLogin(HttpServletRequest httpRequest){
        return new ResponseEntity<>("Authenticated", HttpStatus.OK);
    }
}
