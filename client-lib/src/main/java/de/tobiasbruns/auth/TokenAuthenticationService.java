package de.tobiasbruns.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class TokenAuthenticationService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {


    @Autowired
    private ClientAuthenticationService authService;
    @Autowired(required = false)
    private UserDetailsService userDetailsService;


    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        if (userDetailsService == null)
            return authService.getUserFromToken((String) token.getCredentials());
        else
            return userDetailsService.loadUserByUsername((String) token.getPrincipal());
    }
}
