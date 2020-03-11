package com.rkb.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.sym.Name;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.ribbon.proxy.annotation.Http.Header;
import com.rkb.models.Rating;
import com.rkb.models.UserRating;

@Service
public class UserRatingInfo {

	@Autowired
	private RestTemplate restTemplate;
	
	
	@HystrixCommand(fallbackMethod = "getFallbackUserRating",
			commandProperties = { // command properties is an array of hystrix properties
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"), // it says wait for 2 seconds then if nothing happens call timeout
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"), // it says before applying this look at the last "n" here 5 requests
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"), // it says check whether more than 50% is down or not working
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000") // it says how long the circuit breaker will take time to check whether the main method is working again here it will wait till 5 sec and check in every 5 seconds
			}
			)
	public UserRating getUserRating(String userId) {
		return restTemplate.getForObject("http://rating-data-service/ratings/users/"+userId, UserRating.class);
	}
	
	public UserRating getFallbackUserRating(String userId) {
		UserRating userRating = new UserRating();
		userRating.setUserId(userId);
		userRating.setUserRatings(Arrays.asList(new Rating("0",0)));
		return userRating;
	}
}
