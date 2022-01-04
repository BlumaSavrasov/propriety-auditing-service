package com.example.cheggexercise.services;

import com.example.cheggexercise.model.UserEvent;

public interface ProprietyAuditingService {
    int getAmountOfRequestsInLastHour(String uid);
    UserEvent insert(UserEvent userEventRequest);
}
