package com.rafael.crud.Services;

import com.rafael.crud.DTOs.CreateUserDTO;
import com.rafael.crud.entity.User;
import com.rafael.crud.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    public UUID createUser(CreateUserDTO createUserDTO) {
        var entity = new User(
                null,
                createUserDTO.userName(),
                createUserDTO.fullName(),
                createUserDTO.email(),
                createUserDTO.password(),
                Instant.now(),
                null);

        var userSaved = userRepository.save(entity);

        return userSaved.getUserId();
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.findById(UUID.fromString(userId));
    }
}
