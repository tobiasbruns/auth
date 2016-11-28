package de.tobiasbruns.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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

@Service
class ServerTokenService {

    @Value("${tbauth.key.private}")
    private String privateKeyFile;

    //TODO from configuration
    private int expirationInMin = 30;

    TbAuthToken buildToken(UserDetails user) {
        return buildTokenHolder(user);
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

        return Jwts.builder().setClaims(buildClaimsMap(user))
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(expirationInMin, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.RS256, key).compact();
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
