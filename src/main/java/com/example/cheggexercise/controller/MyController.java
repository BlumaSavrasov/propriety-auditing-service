package com.example.cheggexercise.controller;


import com.example.cheggexercise.model.User;
import com.example.cheggexercise.model.UserDto;
import com.example.cheggexercise.service.MyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class MyController {

    private final MyServiceImpl myService;

    @GetMapping("/get/{uid}")
    int get(@PathVariable String uid){
        return myService.getAmountOfRequest(uid);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/insert")
    User insert(@RequestBody User userDto) {
        return myService.insert(userDto);
    }

}
