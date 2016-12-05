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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class AuthenticateITCase {

	@Autowired
	private WebApplicationContext ctxt;
	@Autowired
	private UserService userService;

	private MockMvc mockMvc;

	@Before
	public void initTest() {
		mockMvc = MockMvcBuilders.webAppContextSetup(ctxt).apply(SecurityMockMvcConfigurers.springSecurity()).build();
		createUser();
	}

	@Test
	public void successfulLogin() throws Exception {
		mockMvc.perform(post(TokenControllerTest.BASE_URL).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(String.format(TokenControllerTest.USER_CREDENTIALS_JSON, "testuser", "geheim"))).andExpect(status().isOk());
	}

	@Test
	public void wrongPassword() throws Exception {
		mockMvc.perform(post(TokenControllerTest.BASE_URL).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(String.format(TokenControllerTest.USER_CREDENTIALS_JSON, "testuser", "wrongpwd"))).andExpect(status().isUnauthorized());
	}

	@Test
	public void refreshToken() throws Exception {
		mockMvc.perform(post(TokenControllerTest.BASE_URL).header("X-Auth-Token", TestToken.VALID_TOKEN.getToken())).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void refreshTokenFromDisabledUser() throws Exception {
		userService.disableUser("f7d1783c-734e-11e6-8b77-86f30ca893d3");

		mockMvc.perform(post(TokenControllerTest.BASE_URL).header("X-Auth-Token", TestToken.VALID_TOKEN.getToken())).andExpect(status().isUnauthorized());
	}

	private void createUser() {
		userService.createUser("testuser", "geheim");
	}
}
