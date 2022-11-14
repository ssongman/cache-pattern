package com.kt.cache.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsersRequestModel {
    private String userId;
    private String userName;
    private Integer age;
    private Integer height;

}
