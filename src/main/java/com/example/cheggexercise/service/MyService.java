package com.example.cheggexercise.service;

import com.example.cheggexercise.model.User;

public interface MyService {
    int getAmountOfRequest(String uid);
    User insert(User userRequest);
}
