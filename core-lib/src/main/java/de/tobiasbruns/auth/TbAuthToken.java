package de.tobiasbruns.auth;

import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

public class TbAuthToken {

    private String token;
    private LocalDateTime issuedAt;
    private LocalDateTime expiration;
    private String username;
    private Collection<? extends GrantedAuthority> authorities = Collections.emptySet();

    TbAuthToken() {

    }

    TbAuthToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }

    public String getUsername() {
        return username;
    }

    void setUsername(String username) {
        this.username = username;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
