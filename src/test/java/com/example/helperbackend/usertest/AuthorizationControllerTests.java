package com.example.helperbackend.usertest;

import com.example.helperbackend.model.User;
import com.example.helperbackend.model.UserRegisterInput;
import com.example.helperbackend.repository.UserRepository;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorizationControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtDecoder jwtDecoder;



    private void saveMockUser(){
        User testUser = new User(
                null,
                "jo0102",
                "Jole",
                "Jocic",
                passwordEncoder.encode("blabla"),
                null,
                "USER",
                null
        );

        userRepository.save(testUser);

    }

    @BeforeAll
    void insertMockUserToRepository(){
        saveMockUser();
    }

    @Test
    void shouldAuthenticate_NewlyRegisteredUser(){


        User mockUser = new User(
                null,
                "user1",
                null,
                null,
                "user123pASWORD",
                null,
                null,
                null
        );



        ResponseEntity<Void> response = restTemplate.postForEntity("/register", mockUser, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<String> getResponse = restTemplate
                .withBasicAuth("user1", "user123pASWORD")
                .getForEntity("/users/user1", String.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        String username = documentContext.read("$.username");
        assertThat(username).isEqualTo("user1");

    }

    @Test
    void shouldNot_RegisterUser_WithInvalidData(){

        User userWithInvalidData = new User(
                null,
                "iv",
                "",
                "",
                "gfdgfd",
                null,
                null,
                null
        );

        ResponseEntity<Void> response = restTemplate.postForEntity("/register", userWithInvalidData, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void shouldNot_RegisterUser_withNonValidPassword(){  // 8 char 1 upper case 1 lowercase at least

        UserRegisterInput userWithInvalidPassword = new UserRegisterInput(
                "ivo111",
                "123456789"
        );

        ResponseEntity<Void> response = restTemplate.postForEntity("/register", userWithInvalidPassword, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldNotCreateUserWithExistingUsername(){

        User mockUser = new User(
                null,
                "jo0102",
                null,
                null,
                "blabla",
                null,
                null,
                null

        );

        ResponseEntity<Void> response = restTemplate.postForEntity("/register", mockUser, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void should_AuthenticateUser_OnLogin_WithBasicAuth(){

        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("jo0102", "blabla")
                .postForEntity("/login", null,  Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void shouldNotAuthenticate_User_WithBadCredentials(){


        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("idontexist", "test")
                .postForEntity("/login", null, Void.class);
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

    @Test
    void shouldProvide_validJWTtoken_toAuthenticatedUser(){

        ResponseEntity<String> response = restTemplate
                .withBasicAuth("jo0102","blabla")
                .getForEntity("/token", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(jwtDecoder.decode(response.getBody())).isNotNull();

        Jwt jwt = jwtDecoder.decode(response.getBody());

        assertThat(jwt.getSubject()).isEqualTo("jo0102");

        assertThat(jwt.getClaims()).containsValue("USER");

        Instant jwtShouldExpire = Instant.now().plus(1, ChronoUnit.HOURS).truncatedTo(ChronoUnit.MINUTES);

        Instant jwtExpires = jwt.getExpiresAt().truncatedTo(ChronoUnit.MINUTES);


        assertThat(jwtShouldExpire).isEqualTo(jwtExpires);


    }

    @Test
    void shouldDeny_returningToken_toUnauthenticatedUser(){

        ResponseEntity<String> response = restTemplate.getForEntity("/token", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        ResponseEntity<String> responseBadCred = restTemplate
                .withBasicAuth("idontexist", "badpassword")
                .getForEntity("/token", String.class);

        assertThat(responseBadCred.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }





}
