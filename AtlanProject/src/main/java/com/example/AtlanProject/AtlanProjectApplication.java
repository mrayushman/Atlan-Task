package com.example.AtlanProject;

import ClientRelatedData.ClientData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.springframework.http.ResponseEntity;

@SpringBootApplication
@EnableWebMvc
@RestController
public class AtlanProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtlanProjectApplication.class, args);
	}

	@CrossOrigin
	@GetMapping("/getSlang")
	public ResponseEntity<String> findSlang(@RequestParam String word, @RequestParam String lang) {
		try {
			String text = translateText(word, lang);
			return ResponseEntity.ok(text);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/validateNew")
	public ResponseEntity<?> validateData(@RequestBody ClientData clientData) {
		try {
			if (clientData.getIncomePerAnnum() < clientData.getSavingsPerAnnum()) {
				return ResponseEntity.badRequest().body("Invalid Data Savings cannot be more than Income");
			} else if (!clientData.getMobileNumber().matches("\\d{10}")) {
				return ResponseEntity.badRequest().body("Invalid mobile number, only 10 digits are acceptable");
			} else {
				// Save clientData to the database
				// Example: clientDataRepository.save(clientData);
				return ResponseEntity.ok("Data saved successfully");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/validateAll")
	public ResponseEntity<?> validateAll() {
		try {
			// Fetch invalid records from the database
			// Example: List<ClientData> invalidData = clientDataRepository.findInvalidData();
			List<ClientData> invalidData = null; // Replace with your database query
			if (invalidData == null || invalidData.isEmpty()) {
				return ResponseEntity.ok("All records are valid");
			} else {
				return ResponseEntity.ok(invalidData);
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/getCSV")
	public ResponseEntity<?> exportCSV() {
		try {
			List<ClientData> data = null; // Fetch data from the database
			if (data != null && !data.isEmpty()) {
				String fileName = "data.csv";
				File file = new File(fileName);
				FileWriter outputfile = new FileWriter(file);
				CSVWriter writer = new CSVWriter(outputfile);
				// Write data to CSV
				// Example: writer.writeAll(data);

				return ResponseEntity.ok(file);
			} else {
				return ResponseEntity.ok("No data to export");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/sendmessage")
	public ResponseEntity<?> sendSMS(@RequestBody ClientData clientData) {
		try {
			String message = "Your Details :\n" +
					"Email ID: " + clientData.getClientEmail() + "\n" +
					"Name: " + clientData.getClientName() + "\n" +
					"Income Per Annum: " + clientData.getIncomePerAnnum() + "\n" +
					"Savings Per Annum: " + clientData.getSavingsPerAnnum() + "\n" +
					"Contact: " + clientData.getMobileNumber() + "\n" +
					"Thank you for your response";
			// Send SMS logic here
			// Example: sendSMS(clientData.getMobileNumber(), message);
			return ResponseEntity.ok("SMS sent successfully");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Failed to send SMS to the Client");
		}
	}

	private String translateText(String word, String lang) throws Exception {
		// Translation logic here yet to be done
		// Yet to be completed
		return "";
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedMethods("GET", "POST");
			}
		};
	}
}


