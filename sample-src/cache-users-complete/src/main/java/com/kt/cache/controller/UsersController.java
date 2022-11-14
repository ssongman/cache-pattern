package com.kt.cache.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.cache.entity.UsersEntity;
import com.kt.cache.model.UsersRequestModel;
import com.kt.cache.model.UsersResponseModel;
import com.kt.cache.service.UsersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users-ms")
public class UsersController {
	 
	private final UsersService usersService;
    
    @GetMapping("/")
    public String health() {
        return "Hi, there. I'm a Users microservice!";
    }

    @GetMapping(value="/users")
    public ResponseEntity<List<UsersResponseModel>> getUsers() {
    	log.info("[UsersController.getUsers]");
        Iterable<UsersEntity> UserList = usersService.getAllUsers();
        List<UsersResponseModel> result = new ArrayList<>();
        UserList.forEach(v -> {
            result.add(new ModelMapper().map(v, UsersResponseModel.class));
        });
        
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    
    @PostMapping(value="/user")
    public ResponseEntity<UsersRequestModel> setUser(@RequestBody UsersRequestModel usersRequestModel) throws JsonProcessingException {
    	log.info("[UsersController.setUser]");
		
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UsersEntity userEntity = modelMapper.map(usersRequestModel, UsersEntity.class);
        userEntity.setCreatedAt(new Date());
        usersService.setUser(userEntity);

        return ResponseEntity.status(HttpStatus.OK).body(usersRequestModel);
    }

    @PatchMapping(value="/user")
    public ResponseEntity<UsersResponseModel> putUser(@RequestBody UsersRequestModel usersRequestModel) throws JsonProcessingException {
    	log.info("[UsersController.putUser]");
    	
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UsersEntity usersEntity = modelMapper.map(usersRequestModel, UsersEntity.class);
        
        // id 찾기
    	long id = usersService.getUser(usersEntity.getUserId()).getId();
    	usersEntity.setId(id);        
    	usersEntity.setCreatedAt(new Date());
    	usersService.putUser(usersEntity);
    	
        UsersResponseModel userResponseModel = modelMapper.map(usersEntity, UsersResponseModel.class);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseModel);
    }
    
    
    @GetMapping(value="/user/{userId}")
    public ResponseEntity<UsersResponseModel> getUser(@PathVariable("userId") String userId) {
    	log.info("[UsersController.getUser]");
    	
    	UsersEntity usersEntity = usersService.getUser(userId);
        
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UsersResponseModel userResponseModel = modelMapper.map(usersEntity, UsersResponseModel.class);
        
        return ResponseEntity.status(HttpStatus.OK).body(userResponseModel);
    }
    
    @DeleteMapping(value="/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId) {
    	log.info("[UsersController.deleteUser]");
        
    	usersService.deleteUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body("delete OK");
    }

}
