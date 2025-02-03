package com.sample.shopease.auth.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sample.shopease.entities.Address;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Table(name = "AUTH_USER_DETAILS")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private UUID id;

  private String firstName;

  private String lastName;

  @JsonIgnore
  private String password;

  private Date createdOn;

  private Date updatedOn;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(unique = true)
  private String phoneNumber;

  private String provider;

  private String verificationCode;

  private boolean enabled=false; //false : 비활성화

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) //EAGER: 즉시 로딩
//  AUTH_USER_AUTHORITY: 다대다 매핑 중간 테이블.
//  joinColumns>현 User 엔티티가 중간테이블에 저장될 때 FK 지정.| inverseJoinColumns>반대 Authority 엔티티가 중간 테이블에 저장될 때 FK 지정
  @JoinTable(name = "AUTH_USER_AUTHORITY", joinColumns = @JoinColumn(referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(referencedColumnName = "id"))
  private List<Authority> authorities;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<Address> addressList;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.email;
  }
}
