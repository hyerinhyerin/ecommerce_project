package com.sample.shopease.auth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//token 관련. OncePerRequestFilter : 한 요청 당 한 번만 실행
public class JWTAuthenticationFilter extends OncePerRequestFilter {

  private final UserDetailsService userDetailsService;
  private final JWTTokenHelper jwtTokenHelper;

  public JWTAuthenticationFilter(JWTTokenHelper jwtTokenHelper, UserDetailsService userDetailsService) {
    this.jwtTokenHelper = jwtTokenHelper;
    this.userDetailsService = userDetailsService;
  }


  @Override
  //요청이 들어올 때마다 실행됨. jwt를 확인하고 검증하는 용도
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    //헤더에서 jwt 토큰 가져오기
    String authHeader = request.getHeader("Authorization");
    if (null == authHeader || !authHeader.startsWith("Bearer")){
      filterChain.doFilter(request, response);
      return;
    }

    try{
      String authToken = jwtTokenHelper.getToken(request); //token 추출
      if (null != authToken){
        String userName = jwtTokenHelper.getUserNameFromToken(authToken); // username(email) 추출
        if (null != userName){
          UserDetails userDetails = userDetailsService.loadUserByUsername(userName); //db 조회

          if (jwtTokenHelper.validateToken(authToken, userDetails)){
            //토큰 유효하다면, spring security 인증 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetails(request)); //WebAuthenticationDetails : 클라이언트 요청 정보(ip 주소 + 세션 id) 담음

            //spring security 컨텍스트에 인증 정보 저장(요청 단위로 유지됨) -> 로그인한 유저의 정보를 모든 클래스에서 사용 가능
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

          }

        }

        filterChain.doFilter(request, response);
      }
    }catch (Exception e){
      throw new RuntimeException(e);
    }
  }
}
