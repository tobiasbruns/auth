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

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Created on 24.08.2016.
 * 
 * @author Tobias Bruns
 */
@Service
class AuthenticationService {

	@Autowired
	private ServerTokenService tokenService;
	@Autowired
	private TbAuthTokenService coreTokenService;
	@Autowired
	private AuthenticationManager authManager;

	public TbAuthToken authenticate(UserCredentials credentials) {
		Authentication auth = authentication(credentials);
		return buildTokenString(auth);

		/*
		 * TbAuthToken token = new TbAuthToken(); UserDetails userDetails =
		 * (UserDetails) auth.getPrincipal();
		 * token.setToken(buildTokenString(auth));
		 * token.setUsername(userDetails.getUsername()); // token.setIssuedAt();
		 * return token;
		 */
	}

	private Authentication authentication(UserCredentials credentials) {
		return authManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));
	}

	private TbAuthToken buildTokenString(Authentication authentication) {
		return tokenService.buildToken((User) authentication.getPrincipal());
	}

	// String authenticateToStringToken(UserCredentials credentials) {
	// Authentication authentication = authManager.authenticate(new
	// UsernamePasswordAuthenticationToken(credentials.getUsername(),
	// credentials.getPassword()));
	// return tokenService.buildToken((UserDetails)
	// authentication.getPrincipal());
	// }
	//
	public TbAuthToken refreshToken(String oldToken) {
		Authentication authentication = authManager.authenticate(new PreAuthenticatedAuthenticationToken(getUserFromToken(oldToken).getUsername(), oldToken));

		return tokenService.buildToken((User) authentication.getPrincipal());
	}

	public User getUserFromToken(String tokenString) {
		TbAuthToken token = coreTokenService.parseToken(tokenString);
		User user = new User();
		user.setUsername(token.getUsername());
		user.setAuthorities(token.getAuthorities().stream().map(auth -> new UserAuthority(auth.getAuthority())).collect(Collectors.toSet()));
		return user;
	}

}
