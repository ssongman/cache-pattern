package com.ssongman.service;

import org.springframework.stereotype.Service;

import com.ssongman.entity.CatalogEntity;
import com.ssongman.repository.CatalogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatalogService{

	private final CatalogRepository repository;
	
	public Iterable<CatalogEntity> getAllCatalogs() {
		log.info("[getAllCatalogs]");
		return repository.findAll();
	}

	public CatalogEntity setCatalog(CatalogEntity catalogEntity){
		log.info("[setCatalog], catalogEntity = {}", catalogEntity);
		repository.save(catalogEntity);
		return catalogEntity;
	}

	public CatalogEntity getCatalog(String productId) {
		log.info("[getCatalog], productId = {}", productId);
		CatalogEntity catalogEntity = repository.findByProductId(productId);
		return catalogEntity;
	}
	
}
