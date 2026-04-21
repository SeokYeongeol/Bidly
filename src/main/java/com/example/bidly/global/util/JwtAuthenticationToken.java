package com.example.bidly.global.util;

import com.example.bidly.global.entity.Auth;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Auth auth;

    public JwtAuthenticationToken(Auth auth) {
        super(auth.getAuthority());
        this.auth = auth;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() { return null; }

    @Override
    public Object getPrincipal() { return this.auth; }
}
