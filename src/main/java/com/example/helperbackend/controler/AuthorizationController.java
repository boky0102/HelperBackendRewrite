package com.example.helperbackend.controler;


import com.example.helperbackend.model.User;
import com.example.helperbackend.repository.UserRepository;
import com.example.helperbackend.service.TokenService;
import com.example.helperbackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("")
public class AuthorizationController {

    private final UserRepository userRepository;

    private final TokenService tokenService;

    private final UserService userService;

    public AuthorizationController(UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder, UserService userService){
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody User postUser){

        userService.registerUser(postUser);

        URI newUserURI = UriComponentsBuilder.fromPath("/users/{username}")
                .buildAndExpand(postUser.username())
                .toUri();

        return ResponseEntity.created(newUserURI).build();
    }

    @GetMapping("/token")
    public String token(Authentication authentication) {
        return tokenService.generateToken(authentication);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> userAuthentication(Principal principal){

        User user = userRepository.findUserByUsername(principal.getName());
        if(user == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();

    }
}
