package com.kt.cache.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kt.cache.dto.UsersDto;

@Repository
public interface UsersCacheRepository extends CrudRepository<UsersDto, String> {

}
