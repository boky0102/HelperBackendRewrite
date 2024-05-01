package com.example.helperbackend.service;


import com.example.helperbackend.exceptions.UserAlreadyExistsException;
import com.example.helperbackend.exceptions.UserDataNotValidException;
import com.example.helperbackend.model.User;
import com.example.helperbackend.model.UserReturnValues;
import com.example.helperbackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(UserService.class);



    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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


    public void registerUser(User user){

        validateUserData(user);

        User userExists = userRepository.findUserByUsername(user.username());

        if(userExists != null){
            throw new UserAlreadyExistsException();
        }

        User savingUser = new User(
                null,
                user.username(),
                user.firstName(),
                user.lastName(),
                passwordEncoder.encode(user.password()),
                null,
                "USER",
                null
        );

        userRepository.save(savingUser);

    }

    public boolean validateUserData(User user) throws UserDataNotValidException {

        String usernamePattern = "^[a-zA-Z0-9]{4,20}$"; // 4 - 20 characters, no special signs

        if(!user.username().matches(usernamePattern)){
            throw new UserDataNotValidException();
        }

        // at least 8 characters, 1 uppercase, 1 lowercase, 1 number
        String passwordPattern = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";
        if(!user.password().matches(passwordPattern)){
            throw new UserDataNotValidException();
        }

        return true;
    }

}
