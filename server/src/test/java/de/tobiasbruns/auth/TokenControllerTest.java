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
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class TokenControllerTest {

	@InjectMocks
	private TokenController controller;
	@Mock
	private AuthenticationService authService;

	private MockMvc mockMvc;

	static final String BASE_URL = "/tokens";
	static final String USER_CREDENTIALS_JSON = "{\"username\":\"%s\",\"password\":\"%s\"}";

	@Before
	public void initTest() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void authenticate() throws Exception {
		TbAuthToken token = new TbAuthToken();
		token.setToken("new token");
		when(authService.authenticate(notNull(UserCredentials.class))).thenReturn(token);

		mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON_UTF8).content(String.format(USER_CREDENTIALS_JSON, "testuser", "geheim")))
				.andExpect(status().isOk()).andExpect(jsonPath("$.token", is("new token"))).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

		ArgumentCaptor<UserCredentials> credentialsCaptor = ArgumentCaptor.forClass(UserCredentials.class);
		verify(authService).authenticate(credentialsCaptor.capture());
		assertThat(credentialsCaptor.getValue()).hasFieldOrPropertyWithValue("username", "testuser").hasFieldOrPropertyWithValue("password", "geheim");
	}

	@Test
	public void refreshToken() throws Exception {
		TbAuthToken newToken = new TbAuthToken();
		newToken.setToken("new token");
		when(authService.refreshToken(notNull(String.class))).thenReturn(newToken);

		mockMvc.perform(post(BASE_URL).header("X-Auth-Token", "old token")).andExpect(status().isOk()).andExpect(jsonPath("$.token", is("new token")));

		ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass(String.class);
		verify(authService).refreshToken(tokenCaptor.capture());
		assertThat(tokenCaptor.getValue()).isEqualTo("old token");
	}
}
