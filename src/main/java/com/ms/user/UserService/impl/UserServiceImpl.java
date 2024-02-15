package com.ms.user.UserService.impl;

import com.ms.user.UserService.entities.User;
import com.ms.user.UserService.exception.ResourceNotFound;
import com.ms.user.UserService.repositories.UserRepo;
import com.ms.user.UserService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public User createUser(User user) {
        return this.userRepo.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepo.findAll();
    }

    @Override
    public User getUserByID(String userId) {
        return userRepo.findById(userId).orElseThrow(() -> new ResourceNotFound("User not found with given user id"+userId) );
    }

    @Override
    public User updateUser(User user , String userId) {

        User user1 = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFound("User not found with given user id"+userId) );

       user1.setName(user.getName());
       user1.setEmail(user.getEmail());
       user1.setAbout(user.getAbout());

        return userRepo.save(user1);

    }

    @Override
    public void deleteUserByID(String userId) {
        User user1 = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFound("User not found with given user id"+ userId) );
        userRepo.delete(user1);

    }
}
