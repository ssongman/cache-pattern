package com.ssongman.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogRequestModel {
    private String productId;
    private String productName;
    private Integer stock;
    private Integer unitPrice;
}
