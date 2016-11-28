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

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class TbAuthTokenService {
	@Value("${tbauth.key.public}")
	private String publicKeyFile;

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
