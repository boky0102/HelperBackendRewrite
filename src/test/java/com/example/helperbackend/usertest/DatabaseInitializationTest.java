package com.example.helperbackend.usertest;

import com.example.helperbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DatabaseInitializationTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void dbShouldBePopulated(){
        var users = userRepository.findAll();

        assertThat(!users.isEmpty()).isEqualTo(true);


    }

}
