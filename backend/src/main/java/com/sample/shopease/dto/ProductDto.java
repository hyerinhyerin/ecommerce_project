package com.sample.shopease.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

  private UUID id;
  private String name;
  private String description;
  private BigDecimal price;
  private String brand;
  private boolean isNewArrival;
  private Float rating;
  private String thumbnail;
  private String slug;
  private UUID categoryId;
  private UUID categoryTypeId;
  private List<ProductVariantDto> variants;
  private List<ProductResourceDto> productResources;
}
