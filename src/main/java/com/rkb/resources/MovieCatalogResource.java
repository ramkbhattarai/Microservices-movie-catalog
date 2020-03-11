package com.rkb.resources;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rkb.models.CatalogItem;
import com.rkb.models.Movie;
import com.rkb.models.Rating;
import com.rkb.models.UserRating;
import com.rkb.services.MovieInfo;
import com.rkb.services.UserRatingInfo;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Autowired
	private MovieInfo movieInfo;
	
	@Autowired
	private UserRatingInfo userRatingInfo;
	
	@GetMapping("/{userId}")
	//@HystrixCommand(fallbackMethod = "getFallbackCatalog") we don't need the fallback here because we have separated the concern
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
		//1. get all rated movie Ids
		
		//2. for each movie id call movie info service and get details
		
		// 3. put them all together 
		
		
		//1. for now we are hard coding the ratings of the movie  so we need a class of rating and hard code 
		//RestTemplate restTemplate = new RestTemplate(); we want single instance of this utility class so we auto wire it by creating bean of it
		
//		List<Rating> ratings = Arrays.asList(  now this information is also obtained by api call to rating data api or microservice
//				new Rating("1234",4),
//				new Rating("1234",4)
//				);
		
		UserRating ratings = userRatingInfo.getUserRating(userId);
		
		// 2. create the rest template
		
		 return ratings.getUserRatings().stream()
				 .map(rating -> {
			 
//		Movie movie = 	restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
//		
//			 // different approach using webclient instead of restTemple. Web client is verbose and does actions asynchronously. 
//			 
////			 Movie movie = webClientBuilder.build()
////			 .get() // this will change depending on get or post methods
////			 .uri("http://localhost:8081/movies/"+rating.getMovieId())
////			 .retrieve()
////			 .bodyToMono(Movie.class)
////			 .block();
//			 
//		return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
					 return movieInfo.getCatalogItem(rating);
		}).collect(Collectors.toList());
		
		
		
//		return Arrays.asList(
//				new CatalogItem("Transformer", "test", 4)
//				);
		 
		
		 
		
	}
	
//	public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId){
//		return Arrays.asList(new CatalogItem("no name", "", 0));
//	}
	
	
	
	// this won't work because the method is called within a method and spring can't make the proxy class by which this
		// circuit breaker pattern is implemented. so we have to make a separate class for this
//	@HystrixCommand(fallbackMethod = "getFallbackUserRating")
//	private UserRating getUserRating(String userId) {
//		return restTemplate.getForObject("http://rating-data-service/ratings/users/"+userId, UserRating.class);
//	}
//	
//	private UserRating getFallbackUserRating(String userId) {
//		UserRating userRating = new UserRating();
//		userRating.setUserId(userId);
//		userRating.setUserRatings(Arrays.asList(new Rating("0",0)));
//		return userRating;
//	}
	
	// this won't work because the method is called within a method and spring can't make the proxy class by which this
	// circuit breaker pattern is implemented. so we have to make a separate class for this
//	@HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
//	private CatalogItem getCatalogItem(Rating rating) {
//		Movie movie = 	restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);	 
//	return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
//	}
//	
//	private CatalogItem getFallbackCatalogItem(Rating rating) {
//		return new CatalogItem("Movie name not found", "", rating.getRating());
//	}

}
