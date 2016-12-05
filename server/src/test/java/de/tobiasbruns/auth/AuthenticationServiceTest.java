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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

	@InjectMocks
	private AuthenticationService service;

	@Mock
	private ServerTokenService tokenService;
	@Mock
	private AuthenticationManager authManager;
	@Mock
	private TbAuthTokenService coreTokenService;
	@Mock
	private Claims claimsMock;
	@Mock
	private User mockUser;
	@Mock
	private Authentication mockAuthentication;

	private static final TbAuthToken VALID_TOKEN = new TbAuthToken();
	private static final TbAuthToken NEW_TOKEN = new TbAuthToken();

	@Before
	public void initTest() {
		when(tokenService.buildToken(notNull(User.class))).thenReturn(NEW_TOKEN);

		when(authManager.authenticate(notNull(Authentication.class))).thenReturn(mockAuthentication);
		when(mockAuthentication.getPrincipal()).thenReturn(mockUser);

		// when(clientAuthService.getUserFromToken(anyString())).thenReturn(mockUser);
	}

	@Test
	public void refreshValidToken() {
		when(coreTokenService.parseToken(anyString())).thenReturn(VALID_TOKEN);

		TbAuthToken newToken = service.refreshToken(VALID_TOKEN.getToken());

		assertThat(newToken).isNotNull().isEqualTo(NEW_TOKEN);
		verify(tokenService).buildToken(eq(mockUser));
	}
}