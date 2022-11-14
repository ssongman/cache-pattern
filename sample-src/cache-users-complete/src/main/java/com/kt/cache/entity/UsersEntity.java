package com.kt.cache.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

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
@Table("USERS")
public class UsersEntity {
    @Id
    private long id;

    private String userId;

    private String userName;

    private Integer age;

    private Integer height;

    private Date createdAt;

}
