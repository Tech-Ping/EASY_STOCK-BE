package com.easystock.backend.presentation.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuthentication extends UsernamePasswordAuthenticationToken {
    private UserAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities){
        super(principal, credentials, authorities);
    }

    public static UserAuthentication createUserAuthentication(Long memberId){
        return new UserAuthentication(memberId, null, null);
    }
}
