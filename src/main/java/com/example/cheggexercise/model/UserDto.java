package com.example.cheggexercise.model;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserDto {
    private String uId;
    private String data;
    private Timestamp timestamp;
}
