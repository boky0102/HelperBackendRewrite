package com.example.helperbackend.controler;


import com.example.helperbackend.model.User;
import com.example.helperbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("")
public class AuthorizationController {

    private final UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public AuthorizationController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

//    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User postUser){

        User user = userRepository.findUserByUsername(postUser.username());

        if(user != null){
            return ResponseEntity.badRequest().build();
        }

        User newUser = new User(
                null,
                postUser.username(),
                postUser.firstName(),
                postUser.lastName(),
                passwordEncoder.encode(postUser.password()),
                null,
                "USER",
                null
        );

        URI newUserURI = UriComponentsBuilder.fromPath("/users/{username}")
                .buildAndExpand(postUser.username())
                .toUri();

        userRepository.save(newUser);
        return ResponseEntity.created(newUserURI).build();
    }
}
