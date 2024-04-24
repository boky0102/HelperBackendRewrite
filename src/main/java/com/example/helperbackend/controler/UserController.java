package com.example.helperbackend.controler;

import com.example.helperbackend.model.User;
import com.example.helperbackend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    UserRepository userRepository;

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
        User user = userRepository.findUserByUserName(username);
        if(user == null){
            return ResponseEntity.notFound().build();
        } else{
            return ResponseEntity.ok(user);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody User user){
        User newUser = new User(null, user.userName(), user.firstName(), user.lastName(), user.password(), null, null);
        userRepository.save(newUser);

        URI newUserURI = UriComponentsBuilder.fromPath("/users/{username}")
                        .buildAndExpand(user.userName())
                        .toUri();

        return ResponseEntity.created(newUserURI).build();

    }
}

