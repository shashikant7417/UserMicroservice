package com.ms.user.UserService.controller;

import com.ms.user.UserService.entities.User;
import com.ms.user.UserService.payload.ApiResponse;
import com.ms.user.UserService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // create User

    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody User user){
        String randomUserId = UUID.randomUUID().toString();
        user.setUserID(randomUserId);
         User user1 = userService.createUser(user);
         return ResponseEntity.status(HttpStatus.CREATED).body(user1);


    }

    // get all user

    @GetMapping("/getAllUser")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok().body(users);
    }

    // get user by id
    @GetMapping("/{userID}")
    public ResponseEntity<User> getUser(@PathVariable String userID){
        User user = userService.getUserByID(userID);
        return ResponseEntity.ok().body(user);

    }

    // update user
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable String userId){

         User updatedUser = userService.updateUser(user, userId);
         return ResponseEntity.ok().body(updatedUser);


    }

    //delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String userId){

        userService.deleteUserByID(userId);
        ApiResponse response = new ApiResponse("User deleted Successfully", true,HttpStatus.OK);
        return ResponseEntity.ok().body(response);

    }
}
