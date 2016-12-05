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

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Tobias on 13.09.2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionTokenFilterTest {

	@InjectMocks
	private SessionTokenFilter filter;
	@Mock
	private ClientAuthenticationService authService;

	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private FilterChain chain;

	private TbAuthUser mockUser = mock(TbAuthUser.class);

	@Before
	public void initTest() {
		when(request.getHeader(SessionTokenFilter.TOKEN_HEADER)).thenReturn("token");
		when(authService.getUserFromToken(anyString())).thenReturn(mockUser);
		when(mockUser.getUsername()).thenReturn("theo");
		when(authService.refreshToken(anyString())).thenReturn(new TbAuthToken("refreshed token"));
	}

	@Test
	public void missingToken() throws IOException, ServletException {
		when(request.getHeader(SessionTokenFilter.TOKEN_HEADER)).thenReturn(null);

		assertThat(filter.getPreAuthenticatedPrincipal(request)).isNull();
	}

	@Test
	public void validTokenPrincipal() {

		assertThat(filter.getPreAuthenticatedPrincipal(request)).isEqualTo("theo");

		verify(authService).getUserFromToken("token");
	}

	@Test
	public void validTokenCredencial() {
		assertThat(filter.getPreAuthenticatedCredentials(request)).isEqualTo("token");
	}

	@Test
	public void refreshToken() throws IOException, ServletException {
		when(response.getStatus()).thenReturn(200);
		filter.refreshToken(request, response);

		verify(response).setHeader(SessionTokenFilter.TOKEN_HEADER, "refreshed token");
	}

	@Test
	public void noNewTokenInFailedRequest() throws IOException, ServletException {
		when(response.getStatus()).thenReturn(404);

		filter.refreshToken(request, response);

		verify(response, never()).setHeader(eq(SessionTokenFilter.TOKEN_HEADER), anyString());
	}
}