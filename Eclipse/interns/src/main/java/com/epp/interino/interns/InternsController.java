package com.epp.interino.interns;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/interns")
public class InternsController {
	
	@GetMapping("/{requestedId}")
	private ResponseEntity<Interns> findById(@PathVariable Long requestedId) {
		if (requestedId.equals(1L)) {
			Interns intern = new Interns(1, "Pepe", "Perez", 1000.00);
			return ResponseEntity.ok(intern);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
}
