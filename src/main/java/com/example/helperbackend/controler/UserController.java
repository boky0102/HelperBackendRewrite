package com.example.helperbackend.controler;

import com.example.helperbackend.model.User;
import com.example.helperbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }



    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers(){

        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserById(@PathVariable String username){
        User user = userRepository.findUserByUsername(username);
        if(user == null){
            return ResponseEntity.notFound().build();
        } else{
            return ResponseEntity.ok(user);
        }
    }

}

