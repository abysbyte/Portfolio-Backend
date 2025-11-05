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
	
	@GetMapping("/download")
	public ResponseEntity<String> downloadResume() {
		try {
			String bucket = "resume";
			String filePath = "VishalThakurResume.pdf";
			String signedUrlJson = supabaseService.generateSignedUrl(bucket, filePath);
			
			// parse JSON returned by Supabase
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(signedUrlJson);
			String signedURL = node.get("signedURL").asText();
			
			// return JSON TO FRONTEND
			Map<String, String> response = new HashMap<>();
			response.put("signedURL", signedURL);
			
			return ResponseEntity.ok(signedUrlJson);
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, String> error = new HashMap<>();
			error.put("error", e.getMessage());
			return ResponseEntity.internalServerError().body("{\"error\":\"" + e.getMessage() + "\"}");
		}
	}
}
