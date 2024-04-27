package com.example.helperbackend.usertest;

import com.example.helperbackend.model.User;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorizationControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    User testUser = new User(
            null,
            "jo0102",
            "Jole",
            "Jocic",
            "blabla",
            null,
            null,
            null
    );

    @Test
    void shouldCreateNewUserOnRegister(){



        ResponseEntity<Void> response = restTemplate.postForEntity("/register",testUser, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<String> getResponse = restTemplate
                .withBasicAuth("jo0102", "blabla")
                .getForEntity("/users/jo0102", String.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        String username = documentContext.read("$.username");
        assertThat(username).isEqualTo("jo0102");

    }

    @Test
    void shouldNotCreateUserWithExistingUsername(){

        ResponseEntity<Void> response = restTemplate.postForEntity("/register", testUser, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @Disabled
    void shouldAuthenticateUserOnLogin(){

        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("jo0102", "blabla")
                .postForEntity("/login", null,  Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void shouldNotAuthenticateNonExistingUser(){

        User nonExistingUser = new User(
                null,
                "idontexist",
                null,
                null,
                "test",
                null,
                null,
                null
        );

        ResponseEntity<Void> response = restTemplate.postForEntity("/login", nonExistingUser, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    }

    @Test
    void shouldNotAuthenticateExistingUserWithBadPassword(){

        User testUser = new User(
                null,
                "jo0102",
                null,
                null,
                "wrongpassword",
                null,
                null,
                null
        );

        ResponseEntity<Void> response = restTemplate.postForEntity("/login", testUser, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);


    }



}
