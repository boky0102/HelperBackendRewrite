package com.example.helperbackend.usertest;


import com.example.helperbackend.model.User;
import com.example.helperbackend.repository.UserRepository;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTests {



    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;




    public void setUpMockUser(){

        // passwordEncoder is null outside the class when run before all test

        User mockUser = new User(
                null,
                "jole0102",
                "Josip",
                "Debic",
                passwordEncoder.encode("joca123"),
                null,
                "USER",
                null
        );

        userRepository.save(mockUser);
    }

    @BeforeAll
    void shouldCreateNewMockUser(){
        setUpMockUser();
    }

    @Test
    void mockUserIsPresent(){
        User user = userRepository.findUserByUsername("jole0102");
        assertThat(user).isNotNull();
    }


    @Test
    void shouldReturnAllUsers(){


        ResponseEntity<String> response = restTemplate
                .withBasicAuth("jole0102", "joca123")
                .getForEntity("/users", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray usernames = documentContext.read("$..username");
        System.out.println(usernames.toString());


        assertThat(usernames).contains("boky0102");


    }

    @Test
    void shouldReturnUserWithGivenUsername(){

        ResponseEntity<String> response = restTemplate
                .withBasicAuth("jole0102", "joca123")
                .getForEntity("/users/boky0102", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String username = documentContext.read("$.username");

        assertThat(username).isEqualTo("boky0102");

    }

    @Test
    void shouldNotReturnAnythingWithNonExistingUsername(){

        ResponseEntity<String> response = restTemplate
                .withBasicAuth("jole0102", "joca123")
                .getForEntity("/users/boky01023", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }



}
