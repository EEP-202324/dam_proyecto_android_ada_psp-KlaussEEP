package com.epp.interino.interns;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

interface InternsRepository extends CrudRepository<Interns, Integer>, 
PagingAndSortingRepository<Interns, Integer> {  
	Interns findByIdAndBoss(Integer id, String boss);
	Page<Interns> findByBoss(String boss, PageRequest pageRequest);
	boolean existsByIdAndBoss(Integer id, String boss);
}

	