package com.kt.cache.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.kt.cache.entity.UsersEntity;
import com.kt.cache.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsersService {
	private final UsersRepository repository;

	@Cacheable(value = "users")     // <-- 추가
	public Iterable<UsersEntity> getAllUsers() {
		log.info("[UsersService.getAllUsers]");
		return repository.findAll();
	}

	@CacheEvict(value="users", allEntries=true)      // <-- 추가
	public UsersEntity setUser(UsersEntity userEntity){
		log.info("[UsersService.setUser], userEntity = {}", userEntity);
		repository.save(userEntity);
		return userEntity;
	}

	@CachePut(value = "users", key = "#userEntity.userId")      // <-- 추가    
	public UsersEntity putUser(UsersEntity userEntity){
		log.info("[UsersService.putUser], userEntity = {}", userEntity);
		repository.save(userEntity);
		return userEntity;
	}

	@Retryable(maxAttempts = 1)   // <-- 추가
	@Cacheable(value = "users", key="#userId")     // <-- 추가
	public UsersEntity getUser(String userId) {
		log.info("[UsersService.deleteUser], userId = {}", userId);
		UsersEntity usersEntity = repository.findByUserId(userId);
		return usersEntity;
	}

    // 아래 getCatalog() 추가
	@Recover
	public UsersEntity getUser(Exception e, String userId) {
		log.info("[UsersService.deleteUser], Fallback Cache, userId = {}", userId);
		UsersEntity usersEntity = repository.findByUserId(userId);
		return usersEntity;
	}
	
	
	
	public void deleteUser(String userId) {
		log.info("[UsersService.deleteUser], userId = {}", userId);
		UsersEntity usersEntity = getUser(userId);
		repository.delete(usersEntity);		
		return;
	}

}
