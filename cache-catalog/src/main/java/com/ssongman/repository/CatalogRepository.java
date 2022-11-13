package com.ssongman.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ssongman.entity.CatalogEntity;

@Repository
public interface CatalogRepository extends CrudRepository<CatalogEntity, Long> {
    CatalogEntity findByProductId(String productId);
}
