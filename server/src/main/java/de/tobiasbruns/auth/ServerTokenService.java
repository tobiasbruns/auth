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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
class ServerTokenService {

	@Value("${tbauth.key.private}")
	private String privateKeyFile;

	// TODO from configuration
	private int expirationInMin = 30;

	TbAuthToken buildToken(UserDetails user) {
		return buildTokenHolder(user);
	}

	@PostConstruct
	public void init() {
		File privateKey = new File(privateKeyFile);
		if (!privateKey.exists()) throw new RuntimeException("Private KeyFile does not exists: " + privateKeyFile);
	}

	private TbAuthToken buildTokenHolder(UserDetails user) {
		LocalDateTime issuedAt = LocalDateTime.now();

		TbAuthToken token = new TbAuthToken();
		token.setUsername(user.getUsername());
		token.setAuthorities(user.getAuthorities());
		token.setIssuedAt(issuedAt);
		token.setExpiration(issuedAt.plus(expirationInMin, ChronoUnit.MINUTES));
		token.setToken(buildTokenString(user));

		return token;
	}

	private String buildTokenString(UserDetails user) {
		Key key = loadPrivateKey(privateKeyFile);

		return Jwts.builder().setClaims(buildClaimsMap(user)).setSubject(user.getUsername()).setIssuedAt(new Date())
				.setExpiration(Date.from(Instant.now().plus(expirationInMin, ChronoUnit.MINUTES))).signWith(SignatureAlgorithm.RS256, key).compact();
	}

	private Map<String, Object> buildClaimsMap(UserDetails user) {
		Map<String, Object> result = new HashMap<>();
		result.put("user", user);
		return result;
	}

	private PrivateKey loadPrivateKey(String filename) {
		byte[] keyBytes;
		try {
			keyBytes = Files.readAllBytes(new File(filename).toPath());

			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			return kf.generatePrivate(spec);
		} catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}
