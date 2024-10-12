package com.amitimtech.urlshortener.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amitimtech.urlshortener.entity.Url;
import com.amitimtech.urlshortener.exception.ResourceNotFoundException;
import com.amitimtech.urlshortener.repository.UrlRepository;

@Service
public class UrlService {

	@Autowired
	private UrlRepository urlRepository;
	
	private static final String BASE_URL = "http://short.url/";
	private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random random = new Random();
	
	public Url createShortUrl(String originalUrl, String createdBy, LocalDateTime dateExpired) {
		Url url = new Url();
		url.setOriginalUrl(originalUrl);
		url.setShortUrl(generateShortUrl());
		url.setDateCreated(LocalDateTime.now());
		url.setDateExpired(dateExpired);
		url.setCreatedBy(createdBy);
		url.setClickCount(0);
		
		return urlRepository.save(url);
	}
	
	public String getOriginalUrl(String shortUrl) {
		shortUrl = BASE_URL + shortUrl;
		Url url = urlRepository.findByShortUrl(shortUrl)
				.orElseThrow(() -> new ResourceNotFoundException("URL not found"));
		
		// Check for expiration
        if (url.getDateExpired() != null && LocalDateTime.now().isAfter(url.getDateExpired())) {
            throw new ResourceNotFoundException("URL has expired");
        }
        
        // Increment click count
        url.setClickCount(url.getClickCount() + 1);
        urlRepository.save(url);

        return url.getOriginalUrl();

	}
	
	public String generateShortUrl() {
        // Generate a random number
        long randomNum = random.nextLong() & Long.MAX_VALUE;  // Use positive long values

        // Encode the number into Base62
        return BASE_URL + toBase62(randomNum);
    }

    // Convert a number into Base62
    private String toBase62(long number) {
        StringBuilder sb = new StringBuilder();
        while (number > 0) {
            sb.append(CHAR_SET.charAt((int) (number % 62)));
            number /= 62;
        }
        return sb.reverse().toString();
    }
}
