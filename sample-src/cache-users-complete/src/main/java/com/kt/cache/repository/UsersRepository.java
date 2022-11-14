package com.kt.cache.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kt.cache.entity.UsersEntity;

@Repository
public interface UsersRepository extends CrudRepository<UsersEntity, Long> {
	UsersEntity findByUserId(String productId);

}
