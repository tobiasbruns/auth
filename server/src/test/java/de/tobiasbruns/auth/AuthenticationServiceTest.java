package de.tobiasbruns.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

	@InjectMocks
	private AuthenticationService service;

	@Mock
	private ServerTokenService tokenService;
	@Mock
	private AuthenticationManager authManager;
	@Mock
	private TbAuthTokenService coreTokenService;
	@Mock
	private Claims claimsMock;
	@Mock
	private User mockUser;
	@Mock
	private Authentication mockAuthentication;

	private static final TbAuthToken VALID_TOKEN = new TbAuthToken();
	private static final TbAuthToken NEW_TOKEN = new TbAuthToken();

	@Before
	public void initTest() {
		when(tokenService.buildToken(notNull(User.class))).thenReturn(NEW_TOKEN);

		when(authManager.authenticate(notNull(Authentication.class))).thenReturn(mockAuthentication);
		when(mockAuthentication.getPrincipal()).thenReturn(mockUser);

		// when(clientAuthService.getUserFromToken(anyString())).thenReturn(mockUser);
	}

	@Test
	public void refreshValidToken() {
		when(coreTokenService.parseToken(anyString())).thenReturn(VALID_TOKEN);

		TbAuthToken newToken = service.refreshToken(VALID_TOKEN.getToken());

		assertThat(newToken).isNotNull().isEqualTo(NEW_TOKEN);
		verify(tokenService).buildToken(eq(mockUser));
	}
}