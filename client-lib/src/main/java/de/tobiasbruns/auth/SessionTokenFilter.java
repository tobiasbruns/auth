/**
 * Copyright (c) 2016, Tobias Bruns
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.tobiasbruns.auth;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;

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
		// refreshToken((HttpServletRequest) request, (HttpServletResponse)
		// response);
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
