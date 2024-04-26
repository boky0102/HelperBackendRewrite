package com.example.helperbackend.model;

import org.springframework.data.annotation.Id;

import java.sql.Timestamp;


public record User (

    @Id
    String uid,

    String username,
    String firstName,
    String lastName,
    String password,

    Timestamp createdAt,

    String role,
    Integer aid
){}
