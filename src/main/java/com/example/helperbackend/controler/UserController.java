package com.example.helperbackend.controler;

import com.example.helperbackend.model.User;
import com.example.helperbackend.model.UserReturnValues;
import com.example.helperbackend.repository.UserRepository;
import com.example.helperbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    private final UserService userService;

    UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService){
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers(){

        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserReturnValues> getUserById(@PathVariable String username){
        UserReturnValues user = userService.getUserData(username);
        if(user == null){
            return ResponseEntity.notFound().build();
        } else{
            return ResponseEntity.ok(user);
        }
    }

}

