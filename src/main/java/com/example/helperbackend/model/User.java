package com.example.helperbackend.model;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;


public record User (

    @Id
    String uid,

    String userName,
    String firstName,
    String lastName,
    String password,
    LocalDate createdAt,
    Integer aid
){}
