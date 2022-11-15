package com.kt.cache.dto;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@RedisHash(value = "users", timeToLive = 60L)
public class UsersDto {
    private long id;

    @Id
    private String userId;

    private String userName;

    private Integer age;

    private Integer height;

    private Date createdAt;

}
