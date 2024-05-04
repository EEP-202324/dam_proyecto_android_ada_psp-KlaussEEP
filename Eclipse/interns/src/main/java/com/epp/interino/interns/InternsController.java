package com.epp.interino.interns;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/interns")
public class InternsController {

	private final InternsRepository internsRepository;

	private InternsController(InternsRepository internsRepository) {
		this.internsRepository = internsRepository;
	}
	
	@GetMapping("/{requestedId}")
	private ResponseEntity<Interns> findById(@PathVariable Integer requestedId) {
		Optional<Interns> internsOptional = internsRepository.findById(requestedId);
		if (internsOptional.isPresent()) {
			return ResponseEntity.ok(internsOptional.get());
		}else {
			return ResponseEntity.notFound().build();
		}
	}
}
	