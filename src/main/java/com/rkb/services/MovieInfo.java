package com.rkb.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rkb.models.CatalogItem;
import com.rkb.models.Movie;
import com.rkb.models.Rating;
import com.rkb.models.UserRating;

@Service
public class MovieInfo {

	@Autowired
	private RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
	public CatalogItem getCatalogItem(Rating rating) {
		Movie movie = 	restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);	 
	return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
	}
	
	public CatalogItem getFallbackCatalogItem(Rating rating) {
		return new CatalogItem("Movie name not found", "", rating.getRating());
	}
}
