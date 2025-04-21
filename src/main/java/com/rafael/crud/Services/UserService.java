package com.rafael.crud.Services;

import com.rafael.crud.DTOs.CreateUserDTO;
import com.rafael.crud.DTOs.UpdateUserDTO;
import com.rafael.crud.entity.User;
import com.rafael.crud.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    public UUID createUser(CreateUserDTO createUserDTO) {
        var entity = new User(
                null,
                createUserDTO.userName(),
                createUserDTO.fullName(),
                createUserDTO.email(),
                encoder.encode(createUserDTO.password()),
                Instant.now(),
                null);

        var userSaved = userRepository.save(entity);

        return userSaved.getUserId();
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.findById(UUID.fromString(userId));
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public void updateUserById(String userId, UpdateUserDTO updateUserDTO) {
        var id = UUID.fromString(userId);
        var userEntity = userRepository.findById(id);

        if (userEntity.isPresent()) {
            var user = userEntity.get();

            if (updateUserDTO.userName() != null) {
                user.setUserName(updateUserDTO.userName());
            }

            if (updateUserDTO.fullName() != null) {
                user.setFullName(updateUserDTO.fullName());
            }

            if (updateUserDTO.password() != null) {
                user.setPassword(encoder.encode(updateUserDTO.password()));
            }

            userRepository.save(user);
        }
    }

    public void deleteUserById(String userId) {
        var id = UUID.fromString(userId);
        var userExists = userRepository.existsById(id);

        if (userExists) {
            userRepository.deleteById(id);
        }
    }
}
