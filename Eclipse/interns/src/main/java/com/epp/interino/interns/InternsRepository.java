package com.epp.interino.interns;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

interface InternsRepository extends CrudRepository<Interns, Integer>, 
PagingAndSortingRepository<Interns, Integer> {  
	
}

	