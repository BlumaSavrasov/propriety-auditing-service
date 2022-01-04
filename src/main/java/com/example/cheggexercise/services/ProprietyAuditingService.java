package com.example.cheggexercise.services;

import com.example.cheggexercise.model.User;

public interface ProprietyAuditingService {
    int getAmountOfRequestsInLastHour(String uid);
    User insert(User userRequest);
}
