package com.ms.user.UserService.controller;

import com.ms.user.UserService.entities.User;
import com.ms.user.UserService.impl.UserServiceImpl;
import com.ms.user.UserService.payload.ApiResponse;
import com.ms.user.UserService.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
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

    private Logger logger = LoggerFactory.getLogger(UserController.class);



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
    int retryCount = 1;
    @GetMapping("/{userID}")
    //@CircuitBreaker(name = "ratingHotelBreaker",fallbackMethod = "ratingHotelFallback")
    //@Retry(name = "ratingHotelService",fallbackMethod = "ratingHotelFallback")
    @RateLimiter(name = "ratingHotelLimiter", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getUser(@PathVariable String userID){
        logger.info("Get single user handler: User-Controller");
        logger.info("Retry count: {}", retryCount);
        retryCount++;
        User user = userService.getUserByID(userID);
        return ResponseEntity.ok().body(user);

    }

    // creating fallback method for circuit breaker
    public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex) {

        logger.info("Fallback is executed because service is down : ", ex.getMessage());
         User user = User.builder()
                .email("abc@gmail.com")
                .name("dummy")
                .about("Service is down")
                .userID("12345")
                .build();

        return new ResponseEntity<>(user, HttpStatus.OK);



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
