package com.example.helperbackend.repository;

import com.example.helperbackend.model.User;
import org.springframework.data.repository.ListCrudRepository;


public interface UserRepository extends ListCrudRepository<User, String> {

    User findUserByUserName(String userName);
}
