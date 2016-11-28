package de.tobiasbruns.auth;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * Created by U109359 on 19.09.2016.
 */
public class TbAuthUser implements UserDetails {

    private static final long serialVersionUID = 1L;
	private String userId;
    private String username;
    private Collection<TbAuthAuthority> authorities;

    TbAuthUser() {

    }

    TbAuthUser(String username, Set<TbAuthAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }


    public Collection<TbAuthAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return "#####";
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAuthorities(Collection<TbAuthAuthority> authorities) {
        this.authorities = authorities;
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
