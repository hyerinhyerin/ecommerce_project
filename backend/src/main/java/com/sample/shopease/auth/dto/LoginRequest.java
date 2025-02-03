package com.sample.shopease.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
  private String userName;
  private CharSequence password; //CharSequence : 문자열 다루는 인페. 구현체-> String, StringBuilder, StringBuffer, CharBuffer 등
}
