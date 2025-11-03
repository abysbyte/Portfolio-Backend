package com.portfolio.bknd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.bknd.service.SupabaseService;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = "https://portfolio-2-0-eight-sage.vercel.app")
public class ResumeController {
	
	@Autowired
	private SupabaseService supabaseService;
	
	@GetMapping("/download")
	public ResponseEntity<String> downloadResume() {
		try {
			String bucket = "resume";
			String filePath = "VishalThakurResume.pdf";
			String signedUrlJson = supabaseService.generateSignedUrl(bucket, filePath);
			return ResponseEntity.ok(signedUrlJson);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("{\"error\":\"" + e.getMessage() + "\"}");
		}
	}
}
