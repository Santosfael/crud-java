package com.rafael.crud.controllers;

import com.rafael.crud.dtos.AuthRequestDTO;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequestDTO authRequest) {
        return ResponseEntity.ok("Login bem-sucedido!");
    }
}

