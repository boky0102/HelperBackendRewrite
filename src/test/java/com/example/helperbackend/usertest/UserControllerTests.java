package com.example.helperbackend.usertest;


import com.example.helperbackend.model.User;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {


    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldReturnAllUsers(){

        ResponseEntity<String> response = restTemplate.getForEntity("/users", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray usernames = documentContext.read("$..userName");


        assertThat(usernames).contains("boky0102");


    }

    @Test
    void shouldReturnUserWithGivenUsername(){

        ResponseEntity<String> response = restTemplate.getForEntity("/users/boky0102", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String username = documentContext.read("$.userName");

        assertThat(username).isEqualTo("boky0102");

    }

    @Test
    void shouldNotReturnAnythingWithNonExistingUsername(){

        ResponseEntity<String> response = restTemplate.getForEntity("/users/boky01023", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @DirtiesContext
    void shouldCreateANewUser(){

        User user = new User(
                null,
                "ivo0102",
                "Ivo",
                "Ribaric",
                "blabla",
                null,
                null
        );

        ResponseEntity<String> response = restTemplate.postForEntity("/users/register", user ,String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNull();
        assertThat(response.getHeaders().getLocation().toString()).isEqualTo("/users/ivo0102");

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/users/ivo0102", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        String username = documentContext.read("$.userName");
        assertThat(username).isEqualTo("ivo0102");

    }




}
