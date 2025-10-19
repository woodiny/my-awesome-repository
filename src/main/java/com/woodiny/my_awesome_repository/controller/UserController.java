package com.woodiny.my_awesome_repository.controller;

import com.woodiny.my_awesome_repository.dto.request.UserRegistrationRequest;
import com.woodiny.my_awesome_repository.dto.response.UserResponse;
import com.woodiny.my_awesome_repository.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    
    private final UserService userService;
    
    @PostMapping
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        log.info("사용자 가입 API 호출: {}", request);
        
        UserResponse response = userService.registerUser(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        log.info("사용자 조회 API 호출: userId={}", userId);
        
        UserResponse response = userService.getUser(userId);
        
        return ResponseEntity.ok(response);
    }
}
