package com.tingyu.security.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class User implements UserDetails {

    private int id;

    private String username;

    private String password;

    private int account_non_expired;

    private int account_non_locked;

    private int credentials_non_expired;

    private int enable;

    private List<String> roleList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorties = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roleList)) {
            for (String role : roleList) {
                authorties.add(new SimpleGrantedAuthority(role));
            }
        }
        return authorties;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return account_non_expired == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return account_non_locked == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentials_non_expired == 1;
    }

    @Override
    public boolean isEnabled() {
        return enable == 1;
    }

}
