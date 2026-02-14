package com.example.friendify_backend_java.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/welcome")
    public ResponseEntity<?> welcome(){
        return ResponseEntity.status(HttpStatus.OK).body(
                Map.of(
                        "message", "welcome to me"
                )
        );
    }
}
