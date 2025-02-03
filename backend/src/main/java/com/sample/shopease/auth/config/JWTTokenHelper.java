package com.sample.shopease.auth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTTokenHelper {
  @Value("${jwt.auth.app}")
  private String appName; //JWT 발급자(issuer) 정보

  @Value("${jwt.auth.secret_key}")
  private String secretKey; //JWT 서명을 위한 비밀키

  @Value("${jwt.auth.expires_in}")
  private int expiresIn; //JWT 만료 시간(초 단위)


  //token 생성
  public String generateToken(String userName){
    return Jwts.builder()
            .issuer(appName) //발급자 설정
            .subject(userName) //사용자 명
            .issuedAt(new Date()) //발급한 시간
            .expiration(generateExpirationDate()) //만료 시간
            .signWith(getSigningKey()) //서명 키 설정
            .compact(); //토큰 생성
  }

  //서명 키 반환
  private Key getSigningKey() {
    byte[] keysBytes = Decoders.BASE64.decode(secretKey); //BASE64로 인코딩된 secretKey를 디코딩
    return Keys.hmacShaKeyFor(keysBytes);
  }

  private Date generateExpirationDate() {
    return new Date(new Date().getTime() + expiresIn * 1000L);
  }

  //요청에서 JWT 추출
  public String getToken(HttpServletRequest request) {
    String authHeader = getAuthHeaderFromHeader(request);
    if (authHeader != null && authHeader.startsWith("Bearer ")){
      return authHeader.substring(7);
    }
    return authHeader;
  }

  //JWT 검증
  public Boolean validateToken(String token, UserDetails userDetails){
    final String username = getUserNameFromToken(token);
    return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  //JWT 만료 여부 확인
  private boolean isTokenExpired(String token) {
    Date expireDate = getExpirationDate(token);
    return expireDate.before(new Date()); //토큰 만료시간이 현재 시간보다 이전인지 체크
  }

  private Date getExpirationDate(String token) {
    Date expireDate;
    try{
      final Claims claims = this.getAllClaimsFromToken(token);
      expireDate = claims.getExpiration();
    } catch (Exception e) {
      expireDate = null;
    }
    return expireDate;
  }

  private String getAuthHeaderFromHeader(HttpServletRequest request) {
    return request.getHeader("Authorization");
  }

  public String getUserNameFromToken(String authToken) {
    String username;
    try{
      final Claims claims = this.getAllClaimsFromToken(authToken);
      username = claims.getSubject();
    } catch (Exception e) {
      username = null;
    }

    return username;
  }

  private Claims getAllClaimsFromToken(String token){
    Claims claims;

    try{
      claims = Jwts.parser() //JWT 파싱 후
              .setSigningKey(getSigningKey()) //서명 키 설정(무결성 검증)
              .build()
              .parseClaimsJws(token) //jwt 파싱하고 검증
              .getBody(); //토큰 페이로드(Claims) 반환
    } catch (Exception e) {
      claims = null;
    }
    return claims;
  }
}
