package com.amitimtech.urlshortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amitimtech.urlshortener.entity.Url;
import com.amitimtech.urlshortener.service.UrlService;

@RestController
@RequestMapping("/api/urls")
public class UrlController {
	
	@Autowired
	private UrlService urlService;
	
	@PostMapping("/shorten")
	public ResponseEntity<Url> createShortUrl(@RequestBody Url url) {
		Url createdUrl = urlService.createShortUrl(url.getOriginalUrl(), url.getCreatedBy(), url.getDateExpired());
		return new ResponseEntity<Url>(createdUrl, HttpStatus.CREATED);
	}
	
//	@GetMapping("/original/{shortUrl}")
//	public ResponseEntity<String> getOriginalUrl(@PathVariable String shortUrl) {
//		String originalUrl = urlService.getOriginalUrl(shortUrl);
//		return new ResponseEntity<String>(originalUrl, HttpStatus.OK);
//	}
	
	@GetMapping("/original/{shortUrl}")
	public ResponseEntity<String> getOriginalUrl(@PathVariable String shortUrl) {
		System.out.println(shortUrl);
		String originalUrl = urlService.getOriginalUrl(shortUrl);
		return new ResponseEntity<String>(originalUrl, HttpStatus.OK);
	}

}
