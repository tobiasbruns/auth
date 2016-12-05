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

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {

	private static final User USER = new User("theo", "geheim", true, true, true, true, Arrays.asList(new TbAuthAuthority("ROLE_TEST_USER")));

	@InjectMocks
	private ServerTokenService service;

	private final String privateKeyFile = "target/test-classes/private_key.der";

	@Before
	public void initTest() {
		ReflectionTestUtils.setField(service, "privateKeyFile", privateKeyFile);
	}

	@Test
	public void buildToken() {
		TbAuthToken token = service.buildToken(USER);

		assertThat(token).isNotNull();
		assertThat(token.getAuthorities()).extracting(GrantedAuthority::getAuthority).containsOnly("ROLE_TEST_USER");
	}

	/*
	 * @Test public void buildAndVerifyToken() { String token =
	 * service.buildToken(USER);
	 * 
	 * Claims claims = service.parseAndVerifyToken(token);
	 * 
	 * assertThat(claims.getSubject()).isEqualTo(USER.getUsername()); //
	 * assertThat(claims.get("user",
	 * TbAuthUser.class)).hasFieldOrPropertyWithValue("username",USER.
	 * getUsername()); }
	 */

	/*
	 * @Test(expected = SignatureException.class) public void invalidToken() {
	 * String token = service.buildToken(USER);
	 * service.parseAndVerifyToken(token.replaceFirst("a","u")); }
	 */
}
