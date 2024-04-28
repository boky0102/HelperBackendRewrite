package com.example.helperbackend.service;


import com.example.helperbackend.model.User;
import com.example.helperbackend.model.UserReturnValues;
import com.example.helperbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;


    UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public  UserReturnValues getUserData(String username){
        User user = userRepository.findUserByUsername(username);
        if(user == null){
            return null;
        } else{
            return new UserReturnValues(
                    user.uid(),
                    user.username(),
                    user.firstName(),
                    user.lastName()
            );
        }
    }

}
