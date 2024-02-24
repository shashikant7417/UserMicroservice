package com.ms.user.UserService.impl;

import com.ms.user.UserService.entities.Hotel;
import com.ms.user.UserService.entities.Rating;
import com.ms.user.UserService.entities.User;
import com.ms.user.UserService.exception.ResourceNotFound;
import com.ms.user.UserService.external.service.HotelService;
import com.ms.user.UserService.repositories.UserRepo;
import com.ms.user.UserService.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.LoggingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    public Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
         User user1 = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFound("User not found with given user id" + userId));
         //http://localhost:8083/rating/user/2d6de082-ef51-4153-8ce4-61c92373bef1
        Rating[] userRating = restTemplate.getForObject("http://RATING-SERVICE/rating/user/"+user1.getUserID(), Rating[].class);
        logger.info("{}",userRating);
         List<Rating> ratings = Arrays.stream(userRating).toList();

        List<Rating> ratingList = ratings.stream().map(rating -> {

            // Api call to hotel service to get hotel
            //http://localhost:8082/hotels/b27f75ce-75c5-4c4a-bdd2-4b54145a857a
          //  ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
            Hotel hotel = hotelService.getHotel(rating.getHotelId());

            // set the hotel to rating
            rating.setHotel(hotel);

            // return rating
            return rating;

        }).toList();

        user1.setRatings(ratingList);
        return user1;
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
