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
