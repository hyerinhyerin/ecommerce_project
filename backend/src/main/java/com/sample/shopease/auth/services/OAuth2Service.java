package com.sample.shopease.auth.services;

import com.sample.shopease.auth.entities.User;
import com.sample.shopease.auth.repositories.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuth2Service {
  @Autowired
  UserDetailRepository userDetailRepository;
  @Autowired
  private AuthorityService authorityService;

  public User getUser(String userName) {
    return userDetailRepository.findByEmail(userName);
  }

  public User createUser(OAuth2User oAuth2User, String provider) {
    //System.out.println("Google User Attributes: " + oAuth2User.getAttributes()); // = 넘어오는 정보 json 형태로 확인하기
    String firstName = oAuth2User.getAttribute("given_name");
    String lastName = oAuth2User.getAttribute("family_name");
    String email = oAuth2User.getAttribute("email");
    User user = User.builder()
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .provider(provider)
            .enabled(true)
            .authorities(authorityService.getUserAuthority())
            .build();
    return userDetailRepository.save(user);
  }
}
