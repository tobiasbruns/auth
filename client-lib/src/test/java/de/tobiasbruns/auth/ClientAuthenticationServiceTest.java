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

import static de.tobiasbruns.auth.TestToken.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@SpringBootTest(classes = TestConfiguration.class)
@RunWith(SpringRunner.class)
public class ClientAuthenticationServiceTest {

	@Autowired
	private ClientAuthenticationService service;

	@Test
	public void validToken() {
		TbAuthUser user = service.getUserFromToken(VALID_TOKEN.getToken());
		assertThat(user).isNotNull().hasFieldOrPropertyWithValue("username", "theo");
		assertThat(user.getAuthorities()).hasSize(1).containsOnlyOnce(new TbAuthAuthority("ROLE_TEST_USER"));
	}

	@Test(expected = ExpiredJwtException.class)
	public void expiredToken() {
		service.getUserFromToken(EXPIRED_TOKEN);
	}

	@Test(expected = SignatureException.class)
	public void manipulatedToken() {
		service.getUserFromToken(MANIPULATED_TOKEN);
	}

	@Ignore("needs a running token service")
	@Test
	public void refreshToken() {
		TbAuthToken newToken = service.refreshToken(VALID_TOKEN.getToken());

		assertThat(newToken).isNotNull().isNotEqualTo(VALID_TOKEN);
	}
}
