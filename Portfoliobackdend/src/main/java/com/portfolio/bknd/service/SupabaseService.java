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
	        .connectTimeout(java.time.Duration.ofSeconds(10)) // Add a connection timeout
	        .build();
	
	/**
	 * Generates a signed URL for a file in Supabase storage.
	 *
	 * @param bucket The name of the storage bucket.
	 * @param filePath The path and name of the file within the bucket.
	 * @return The raw JSON response body from Supabase (which should contain the signed URL).
	 * @throws Exception if the request fails, times out, or returns a non-200 status code.
	 */
	public String generateSignedUrl(String bucket, String filePath) throws Exception {
		
		// 1. Basic configuration check
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
				.timeout(java.time.Duration.ofSeconds(15)) // Add a request timeout
				.POST(HttpRequest.BodyPublishers.ofString(requestBody))
				.build();
		
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		
		// 2. CRITICAL FIX: Check the status code!
		if (response.statusCode() != 200) {
			// Throw a detailed exception if Supabase returns an error
			throw new RuntimeException(
				"Supabase API request failed. Status Code: " + response.statusCode() + 
				". Response Body: " + response.body()
			);
		}
		
		// 3. Return the body (should now be clean JSON)
		return response.body();
	}
}