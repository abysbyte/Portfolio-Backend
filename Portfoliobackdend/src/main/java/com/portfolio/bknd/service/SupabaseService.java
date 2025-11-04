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
	
	private final HttpClient httpClient = HttpClient.newHttpClient();
	
	public String generateSignedUrl(String bucket, String filePath) throws Exception {
		String requestBody = "{\"expiresIn\": 3600}";
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(supabaseUrl + "/storage/v1/object/sign/" + bucket + "/" + filePath))
				.header("Authorization", "Bearer " + supabaseKey)
				.header("apikey", supabaseKey)
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(requestBody))
				.build();
		
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		return response.body();
	}
	
}
