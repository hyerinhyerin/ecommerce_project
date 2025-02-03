package com.sample.shopease.auth.config;

import com.sample.shopease.auth.exceptions.RESTAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private JWTTokenHelper jwtTokenHelper;

  private static final String[] publicApis = {
          "/api/auth/**"
  };

  //  경로 보안 설정
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((authorize) -> authorize
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/products", "/api/category").permitAll()
                    .requestMatchers("/oauth2/success").permitAll()
                    .anyRequest().authenticated())
            .oauth2Login((oauth2login) -> oauth2login.defaultSuccessUrl("/oauth2/success").loginPage("/oauth2/authorization/google")) //소셜 성공하면 해당 경로로 리디렉션
            .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .exceptionHandling((exception) -> exception.authenticationEntryPoint(new RESTAuthenticationEntryPoint()))
            .addFilterBefore(new JWTAuthenticationFilter(jwtTokenHelper, userDetailsService), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web -> web.ignoring().requestMatchers(publicApis));
  }


  //  로그인 시, ProviderManager가 DaoAuthenticationProvider를 사용해 인증 수행
  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(); //AuthenticationProvider 구현체
    daoAuthenticationProvider.setUserDetailsService(userDetailsService); //사용자 정보 로드
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder()); //비밀번호 검증

    //인증 성공 시, Authentication 객체 반환(token)
    //로그인 시 여러 AuthenticationProvider 중 적절한 것을 찾아 인증을 수행. > 현 dao 하나 등록이라 이 provider로 인증
    return new ProviderManager(daoAuthenticationProvider); //ProviderManager: AuthenticationManger 구현체 + 여러 AuthenticationProvider 관리
  }


  //  비밀번호 암호화
  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
