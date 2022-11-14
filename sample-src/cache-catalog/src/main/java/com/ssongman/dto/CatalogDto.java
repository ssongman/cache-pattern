package com.ssongman.dto;

import java.util.Date;

import org.springframework.data.annotation.Id;

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
public class CatalogDto {

	private long id;

	@Id
	private String productId;

	private String productName;

	private Integer stock;

	private Integer unitPrice;

	private Date createdAt;
	
	
}