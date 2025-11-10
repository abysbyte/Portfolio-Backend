package com.portfolio.bknd.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SupabaseService {
	
	@Value("${supabase.url}")
	private String supabaseUrl;
	
	@Value("${supabase.key}")
	private String supabaseKey;
	
	// Create a single client instance for reuse
	private final HttpClient httpClient = HttpClient.newBuilder()
	        .connectTimeout(java.time.Duration.ofSeconds(60)) // Add a connection timeout
	        .build();
	
	
	public String generateSignedUrl(String bucket, String filePath) throws Exception {
		
		if (supabaseUrl == null || supabaseKey == null || supabaseUrl.isBlank() || supabaseKey.isBlank()) {
			throw new IllegalStateException("Supabase URL or Key environment variables are not set correctly.");
		}
		
		String requestBody = "{\"expiresIn\": 3600}";
		String fullUrl = supabaseUrl + "/storage/v1/object/sign/" + bucket + "/" + filePath;
		
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(fullUrl))
				.header("Authorization", "Bearer " + supabaseKey)
				.header("apikey", supabaseKey)
				.header("Content-Type", "application/json")
				.timeout(java.time.Duration.ofSeconds(15)) // request timeout
				.POST(HttpRequest.BodyPublishers.ofString(requestBody))
				.build();
		
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		
		if (response.statusCode() != 200) {
			// Throw a detailed exception if Supabase returns an error
			throw new RuntimeException(
				"Supabase API request failed. Status Code: " + response.statusCode() + 
				". Response Body: " + response.body()
			);
		}
		
		return response.body();
	}
}