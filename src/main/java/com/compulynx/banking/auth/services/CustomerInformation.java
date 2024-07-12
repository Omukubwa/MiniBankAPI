package com.compulynx.banking.auth.services;

import com.compulynx.banking.auth.models.Customer;
import com.compulynx.banking.utils.UtilityService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public class CustomerInformation implements UserDetails {
    private final String name;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public CustomerInformation(Customer userInfo) {
        var utilityService = new UtilityService();
        name = userInfo.getCustomerId();
        password = userInfo.getPin();
        authorities = utilityService.mapUserAuthorities(userInfo.getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
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

