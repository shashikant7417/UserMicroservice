package com.ms.user.UserService.service;

import com.ms.user.UserService.entities.User;

import java.util.List;

public interface UserService {

    //create User

    User createUser(User user);

    // get all Users

    List<User> getAllUser();

    // get user by id

    User getUserByID(String id);

    // update User

    User updateUser(User user, String userId);

    // delete User

    void deleteUserByID(String id);
}
