package com.example.backendtest.controllers;

import com.example.backendtest.dtos.UserRequest;
import com.example.backendtest.dtos.ResponseDTO;
import com.example.backendtest.services.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createUser(@RequestBody @Valid UserRequest userRequest){

        return userService.createUser(userRequest);
    }

    @GetMapping("/filter/{providerId}")
    public ResponseEntity<ResponseDTO> filterUser(@PathVariable @Valid @NotNull(message = "ProviderId cannot be null")
                                                              int providerId, String name,
                                                  String age, String timestamp ){

        return userService.getUsers(providerId, name, age, timestamp);
    }


}
