package com.sample.shopease.controllers;

import com.sample.shopease.dto.ProductDto;
import com.sample.shopease.entities.Product;
import com.sample.shopease.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;

  @GetMapping
  public ResponseEntity<List<ProductDto>> getAllProducts(@RequestParam(required = false, name = "categoryId") UUID categoryId,
                                                         @RequestParam(required = false, value = "typeId") UUID typeId,
                                                         @RequestParam(required = false,name = "slug") String slug) {
    List<ProductDto> productList = new ArrayList<>();
    if (StringUtils.isNotBlank(slug)) {
      ProductDto productDto = productService.getProductBySlug(slug);
      productList.add(productDto);
    }
    else{
      productList = productService.getAllProducts(categoryId, typeId);
    }
    return new ResponseEntity<>(productList, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDto> getProductById(@PathVariable UUID id){
    ProductDto productDto = productService.getProductById(id);
    return new ResponseEntity<>(productDto, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto) {
    Product product = productService.addProduct(productDto);
    return new ResponseEntity<>(product, HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<Product> updateProduct(@RequestBody ProductDto productDto){
    Product product = productService.updateProduct(productDto);
    return new ResponseEntity<>(product, HttpStatus.OK);
  }
}
