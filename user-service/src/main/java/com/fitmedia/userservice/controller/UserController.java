package com.fitmedia.userservice.controller;

import com.fitmedia.userservice.dto.RegisterRequest;
import com.fitmedia.userservice.dto.UserResponse;
import com.fitmedia.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @GetMapping("/{userId}/validade")
    public ResponseEntity<Boolean> validateUser(@PathVariable Long userId){
        return ResponseEntity.ok(userService.existByUserId(userId));
    }

}
