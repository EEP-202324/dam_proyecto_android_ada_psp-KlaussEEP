package com.epp.interino.interns;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/interns")
public class InternsController {

	private final InternsRepository internsRepository;

	private InternsController(InternsRepository internsRepository) {
		this.internsRepository = internsRepository;
		
	}
	
	@GetMapping("/{requestedId}")
	private ResponseEntity<Interns> findById(@PathVariable Integer requestedId, Principal principal) {
		Interns intern = findIntern(requestedId, principal);
	    if (intern != null) {
	        return ResponseEntity.ok(intern);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
	@PostMapping
	private ResponseEntity<Void> createIntern(@RequestBody Interns newInternRequest, UriComponentsBuilder ucb, Principal principal) {
		Interns internsWithOwner = new Interns(newInternRequest.id(), newInternRequest.name(), newInternRequest.surname(), newInternRequest.amount(), principal.getName());
	    Interns savedIntern = internsRepository.save(internsWithOwner);   
		URI locationOfNewIntern = ucb
			.path("interns/{id}")
			.buildAndExpand(savedIntern.id())
			.toUri();
		return ResponseEntity.created(locationOfNewIntern).build();
	}
	
	@GetMapping
	private ResponseEntity<List<Interns>> findAll(Pageable pageable, Principal principal) {
	    Page<Interns> page = internsRepository.findByBoss(principal.getName(),
	            PageRequest.of(
	                    pageable.getPageNumber(),
	                    pageable.getPageSize(),
	                    pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))
	    ));
	    return ResponseEntity.ok(page.getContent());
	}
	
	@PutMapping("/{requestedId}")
	private ResponseEntity<Void> putIntern(@PathVariable Integer requestedId, @RequestBody Interns internUpdate, Principal principal) {
		Interns intern = findIntern(requestedId, principal);
	    if (intern != null) {
	    	Interns updatedIntern = new Interns(intern.id(), internUpdate.name(), internUpdate.surname(), internUpdate.amount(), principal.getName());
	        internsRepository.save(updatedIntern);
	        return ResponseEntity.noContent().build();
	    }
	    return ResponseEntity.notFound().build();
	}
	
	private Interns findIntern(Integer requestedId, Principal principal) {
	    return internsRepository.findByIdAndBoss(requestedId, principal.getName());
	}
	
	@DeleteMapping("/{id}")
	private ResponseEntity<Void> deleteCashCard(@PathVariable Integer id,  Principal principal) {
		if (internsRepository.existsByIdAndBoss(id, principal.getName())) {
			internsRepository.deleteById(id);
			return ResponseEntity.noContent().build(); 
	    }
		return ResponseEntity.notFound().build();
	}
	
}
	