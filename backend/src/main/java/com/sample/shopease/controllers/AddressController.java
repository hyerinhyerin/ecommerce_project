package com.sample.shopease.controllers;

import com.sample.shopease.dto.AddressRequest;
import com.sample.shopease.entities.Address;
import com.sample.shopease.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/address")
public class AddressController {
  @Autowired
  private AddressService addressService;

  @PostMapping
  public ResponseEntity<Address> createAddress(@RequestBody AddressRequest addressRequest, Principal principal){
    Address address = addressService.createAddress(addressRequest, principal);
    return new ResponseEntity<>(address,HttpStatus.OK);
  }
}
