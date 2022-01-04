package com.example.cheggexercise.controller;


import com.example.cheggexercise.model.User;
import com.example.cheggexercise.services.ProprietyAuditingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@RestController
@RequiredArgsConstructor
public class ProprietyAuditingController {

    private final ProprietyAuditingService myService;

    @GetMapping("/get/{uid}")
    int get(@PathVariable String uid){
        return myService.getAmountOfRequestsInLastHour(uid);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/insert")
    User insert(@RequestBody User user) {
        user.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        return myService.insert(user);
    }

}
