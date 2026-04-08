package com.fitmedia.userservice.service;

import com.fitmedia.userservice.dto.RegisterRequest;
import com.fitmedia.userservice.dto.UserResponse;
import com.fitmedia.userservice.model.User;
import com.fitmedia.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserResponse registerUser(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            User existingUser = userRepository.findByEmail(request.getEmail());
            existingUser = requestToUser(request, existingUser);
            UserResponse userResponse = new UserResponse();
            return userObjToResponse(existingUser, userResponse);
        }

        User user = new User();
        user = requestToUser(request, user);
        User savedUser = userRepository.save(user);
        UserResponse response = new UserResponse();
        return userObjToResponse(savedUser, response);
    }

    public UserResponse getUserProfile(String userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        UserResponse response = new UserResponse();
        return userObjToResponse(user, response);
    }

    public Boolean existByUserId(String userId) {
        log.info("Calling User Validation API for userId: {}", userId);
        return userRepository.existsByKeycloakId(userId);
    }

    private User requestToUser(RegisterRequest request, User user){
        user.setEmail(request.getEmail());
        user.setKeycloakId(request.getKeycloakId());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return user;
    }

    private UserResponse userObjToResponse(User user, UserResponse response){
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setKeycloakId(user.getKeycloakId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
