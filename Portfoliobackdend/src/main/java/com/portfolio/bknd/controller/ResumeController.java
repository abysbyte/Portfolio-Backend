package com.portfolio.bknd.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.bknd.service.SupabaseService;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {
	
	@Autowired
	private SupabaseService supabaseService;

    // ObjectMapper is thread-safe and expensive to create. 
    // Initialize it once for better performance.
    private static final ObjectMapper MAPPER = new ObjectMapper(); 
	
	@GetMapping("/download")
	public ResponseEntity<Map<String, String>> downloadResume() {
		try {
			String bucket = "resume";
			String filePath = "VishalThakurResume.pdf";
			
            // Get the raw signed URL JSON from the service
			String signedUrlJson = supabaseService.generateSignedUrl(bucket, filePath);
			
			System.out.println("Supabase raw response: " + signedUrlJson);
			
			// Parse JSON returned by Supabase using the shared MAPPER
			JsonNode node = MAPPER.readTree(signedUrlJson);
			
			String signedURL = null;
            // Check common key variations returned by Supabase REST API
            if (node.has("signedURL")) {
                signedURL = node.get("signedURL").asText();
            } else if (node.has("signedUrl")) {
                signedURL = node.get("signedUrl").asText();
            } else if (node.has("url")) {
                signedURL = node.get("url").asText();
            }

            if (signedURL == null || signedURL.isBlank()) {
                throw new RuntimeException("Supabase did not return a valid signed URL: " + signedUrlJson);
            }

            // Return a clean JSON object to frontend
            Map<String, String> response = new HashMap<>();
            response.put("signedURL", signedURL);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            // This ensures the detailed error from SupabaseService is passed back to the client logs.
            return ResponseEntity.internalServerError().body(error); 
        }
	}
}