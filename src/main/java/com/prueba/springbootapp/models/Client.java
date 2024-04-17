package com.prueba.springbootapp.models;

import java.math.BigInteger;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;


import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Entity
public class Client implements UserDetails{

    @Setter(AccessLevel.NONE)
    @Id
    @Column(columnDefinition = "BIGINT", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @NotBlank(message = "Identification document number can't be null")
    @Column(name = "Document-Number-ID")
    private String documentNumberId;
    
    @Pattern(regexp = "^([^\\s\\d_][\\p{L}\\p{M}]+(?:\\s+[^\\s\\d_][\\p{L}\\p{M}]+)*)",
        message = "The name must contain only alphabetic characters")
    @NotBlank(message = "Name can't be null")
    private String name;
    
    @Column(name = "Phone-Number")
    private String phoneNumber;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Role role;

    private String password;

    public Client(String documentNumberId, String name, String phoneNumber, Role role, String password) {
        this.documentNumberId = documentNumberId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.documentNumberId;
    }

    @Override
    public boolean isAccountNonExpired() {
       return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;   
    }
    
}
