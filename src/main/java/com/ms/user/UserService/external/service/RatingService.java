package com.ms.user.UserService.external.service;

import com.ms.user.UserService.entities.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("RATING-SERVICE")
public interface RatingService {

    // get

    // post

    @PostMapping("/rating/create")
    Rating createRating(Rating rating);

    // put

    Rating updateRating();
}
