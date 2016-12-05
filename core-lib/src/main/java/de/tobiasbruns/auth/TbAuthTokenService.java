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
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class TbAuthTokenService {
	@Value("${tbauth.key.public}")
	private String publicKeyFile;

	@PostConstruct
	public void init() {
		File keyFile = new File(publicKeyFile);
		if (!keyFile.exists()) throw new RuntimeException("Public Key File does not exists: " + publicKeyFile);
	}

	public TbAuthToken parseToken(String token) {
		Claims claims = parseAndVerifyToken(token);
		TbAuthToken tokenObj = buildToken(claims);
		tokenObj.setToken(token);
		return tokenObj;
	}

	@SuppressWarnings("unchecked")
	private TbAuthToken buildToken(Claims claims) {
		Map<String, ?> userPropertyMap = claims.get("user", Map.class);

		TbAuthToken token = new TbAuthToken();
		token.setIssuedAt(LocalDateTime.ofInstant(claims.getIssuedAt().toInstant(), ZoneId.systemDefault()));
		token.setExpiration(LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault()));
		token.setUsername(claims.getSubject());
		token.setAuthorities(mapAuthorities((List<Map<String, ?>>) userPropertyMap.get("authorities")));
		return token;
	}

	private Collection<TbAuthAuthority> mapAuthorities(List<Map<String, ?>> properties) {
		return properties.stream().map(props -> populate(new TbAuthAuthority(), props)).collect(Collectors.toSet());
	}

	private <T> T populate(T bean, Map<String, ?> properties) {
		try {
			BeanUtils.populate(bean, properties);
			return bean;
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Error reading User from Token.", e);
		}
	}

	private Claims parseAndVerifyToken(String token) {
		Key key = loadPublicKey(publicKeyFile);
		return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
	}

	private PublicKey loadPublicKey(String filename) {
		try {
			byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());

			X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			return kf.generatePublic(spec);
		} catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
