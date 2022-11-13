package com.ssongman.service;

import java.util.Date;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.ssongman.entity.CatalogEntity;
import com.ssongman.repository.CatalogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatalogService{

	private final CatalogRepository catalogRepository;

	@Cacheable(value = "catalog")     // <-- 추가
	public Iterable<CatalogEntity> getAllCatalogs() {
		log.info("[CatalogService.getAllCatalogs]");
		return catalogRepository.findAll();
	}

	@CacheEvict(value="catalog", allEntries=true)      // <-- 추가
	public CatalogEntity setCatalog(CatalogEntity catalogEntity){
		log.info("[CatalogService.setCatalog], catalogEntity = {}", catalogEntity);
		catalogRepository.save(catalogEntity);
		return catalogEntity;
	}

	@CachePut(value = "catalog", key = "#catalogEntity.productId")
	public CatalogEntity putCatalog(CatalogEntity catalogEntity){
		log.info("[CatalogService.setCatalog], catalogEntity = {}", catalogEntity);
		catalogRepository.save(catalogEntity);
		return catalogEntity;
	}

	@Retryable(maxAttempts = 1)   // <-- 추가
	@Cacheable(value = "catalog", key = "#productId")
	public CatalogEntity getCatalog(String productId) {
		log.info("[CatalogService.getCatalog], productId = {}", productId);
		CatalogEntity catalogEntity = catalogRepository.findByProductId(productId);
		return catalogEntity;
	}

    // 아래 getCatalog() 추가
	@Recover
	public CatalogEntity getCatalog(Exception e, String productId) {
		log.info("[CatalogService.getCatalog], Fallback Cache, productId = {}", productId);
		CatalogEntity catalogEntity = catalogRepository.findByProductId(productId);
		return catalogEntity;
	}	
	

	@CacheEvict(value = "catalog", key = "#productId")
	public void deleteCatalog(String productId) {
		log.info("[CatalogService.deleteCatalog], productId = {}", productId);		

		CatalogEntity catalogEntity = getCatalog(productId);
		catalogRepository.delete(catalogEntity);
		
		return;
	}
	
}
