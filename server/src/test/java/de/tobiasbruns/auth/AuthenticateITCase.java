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
