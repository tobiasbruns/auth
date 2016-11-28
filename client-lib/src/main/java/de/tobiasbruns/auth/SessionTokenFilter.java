/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tobiasbruns.auth;


import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class SessionTokenFilter extends AbstractPreAuthenticatedProcessingFilter {

    static final String TOKEN_HEADER = "X-Auth-Token";

    @Autowired
    private ClientAuthenticationService authService;
    @Autowired
    private AuthenticationManager authManager;

    private static final Predicate<Integer> HTTP_STATUS_IS_OK = httpStatus -> httpStatus >= 200 && httpStatus < 300;

    @PostConstruct
    void initBean() {
        super.setAuthenticationManager(authManager);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        refreshToken((HttpServletRequest) request, (HttpServletResponse) response);
        super.doFilter(request, response, chain);

    }

    void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        getTokenFromRequest((HttpServletRequest) request).ifPresent(token -> {
            if (HTTP_STATUS_IS_OK.test(response.getStatus())) {
                response.setHeader(TOKEN_HEADER, authService.refreshToken(token).getToken());
            }
        });
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        try {
            return getTokenFromRequest(request).map(token -> authService.getUserFromToken(token).getUsername()).orElse(null);
        } catch (ExpiredJwtException e) {
            throw new SessionExpiredException(e);
        }
    }


    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return getTokenFromRequest(request).orElse("");
    }


    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(TOKEN_HEADER));
    }


}
