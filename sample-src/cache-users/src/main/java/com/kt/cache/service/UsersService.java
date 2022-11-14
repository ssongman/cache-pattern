package com.kt.cache.service;

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
	
	public Iterable<UsersEntity> getAllUsers() {
		log.info("[UsersService.getAllUsers]");
		return repository.findAll();
	}

	public UsersEntity setUser(UsersEntity userEntity){
		log.info("[UsersService.setUser], userEntity = {}", userEntity);
		repository.save(userEntity);
		return userEntity;
	}

	public UsersEntity putUser(UsersEntity userEntity){
		log.info("[UsersService.putUser], userEntity = {}", userEntity);
		repository.save(userEntity);
		return userEntity;
	}

	public UsersEntity getUser(String userId) {
		log.info("[UsersService.deleteUser], userId = {}", userId);
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
