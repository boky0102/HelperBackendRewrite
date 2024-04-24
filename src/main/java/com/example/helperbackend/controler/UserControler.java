package com.example.helperbackend.controler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/users")
public class UserControler {

    @GetMapping("")
    ResponseEntity<String> getAllUsers(){
        return ResponseEntity.ok().build();
    }

}
