package com.sample.shopease.services;

import com.sample.shopease.auth.entities.User;
import com.sample.shopease.dto.AddressRequest;
import com.sample.shopease.entities.Address;
import com.sample.shopease.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class AddressService {
  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private AddressRepository addressRepository;

  public Address createAddress(AddressRequest addressRequest, Principal principal){
    User user = (User) userDetailsService.loadUserByUsername(principal.getName());
    Address address = Address.builder()
            .street(addressRequest.getStreet())
            .city(addressRequest.getCity())
            .state(addressRequest.getState())
            .zipCode(addressRequest.getZipCode())
            .phoneNumber(addressRequest.getPhoneNumber())
            .user(user)
            .build();
    return addressRepository.save(address);
  }
}
