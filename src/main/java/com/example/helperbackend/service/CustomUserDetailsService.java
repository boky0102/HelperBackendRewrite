package com.example.helperbackend.service;

import com.example.helperbackend.config.CustomUserPrincipal;
import com.example.helperbackend.model.User;
import com.example.helperbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService  implements UserDetailsService {

    @Autowired
    private  UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username){
        User user = userRepository.findUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("user not found" + username);
        } else{
            return new CustomUserPrincipal(user);
        }

    }

}
