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
