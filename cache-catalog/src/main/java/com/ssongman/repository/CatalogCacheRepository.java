package com.ssongman.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ssongman.dto.CatalogDto;

@Repository
public interface CatalogCacheRepository extends CrudRepository<CatalogDto, String> {

}
