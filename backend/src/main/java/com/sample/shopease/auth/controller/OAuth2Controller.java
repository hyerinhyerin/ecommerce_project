package com.sample.shopease.auth.controller;

import com.sample.shopease.auth.config.JWTTokenHelper;
import com.sample.shopease.auth.entities.User;
import com.sample.shopease.auth.services.OAuth2Service;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {
  @Autowired
  OAuth2Service oAuth2Service;
  @Autowired
  private JWTTokenHelper jwtTokenHelper; //객체 캡슐화를 위해 private 붙이는게 좋음


  @GetMapping("/success")
  //@AuthenticationPrincipal : SecurityContextHolder.getContext().getAuthentication().getPrincipal()를 내부적으로 실행해 가져옴
  // = 로그인할 때 인증된 사용자 정보를 담은 context에서 가져오는 것
  //OAuth2User : OAuth2 인증 서버(Google, Facebook 등)에서 제공하는 사용자 정보 객체(Map<String, Object>)
  public void callbackOAuth2(@AuthenticationPrincipal OAuth2User oAuth2User, HttpServletResponse response) throws IOException {
    String userName = oAuth2User.getAttribute("email");
    User user = oAuth2Service.getUser(userName);

    if (null == user){
      user = oAuth2Service.createUser(oAuth2User,"google");
    }

    String token = jwtTokenHelper.generateToken(user.getUsername());

    response.sendRedirect("http://localhost:3000/oauth2/callback?token="+token);
  }
}
