package com.rafael.crud.services;

import com.rafael.crud.dtos.CreateUserDTO;
import com.rafael.crud.dtos.UpdateUserDTO;
import com.rafael.crud.entity.User;
import com.rafael.crud.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UUID createUser(CreateUserDTO createUserDTO) {
        var entity = new User(
                null,
                createUserDTO.userName(),
                createUserDTO.fullName(),
                createUserDTO.email(),
                passwordEncoder.encode(createUserDTO.password()),
                Instant.now(),
                null);

        if (userRepository.existsByEmail(entity.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }

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
                user.setPassword(passwordEncoder.encode(updateUserDTO.password()));
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
