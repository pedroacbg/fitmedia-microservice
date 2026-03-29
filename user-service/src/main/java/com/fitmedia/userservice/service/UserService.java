package com.fitmedia.userservice.service;

import com.fitmedia.userservice.dto.RegisterRequest;
import com.fitmedia.userservice.dto.UserResponse;
import com.fitmedia.userservice.model.User;
import com.fitmedia.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse registerUser(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user = requestToUser(request, user);
        User savedUser = userRepository.save(user);
        UserResponse response = new UserResponse();
        return userObjToResponse(savedUser, response);
    }

    public UserResponse getUserProfile(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        UserResponse response = new UserResponse();
        return userObjToResponse(user, response);
    }

    public Boolean existByUserId(Long userId) {
        return userRepository.existsById(userId);
    }

    private User requestToUser(RegisterRequest request, User user){
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return user;
    }

    private UserResponse userObjToResponse(User user, UserResponse response){
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

}
