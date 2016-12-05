package de.tobiasbruns.utils.authentication.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import de.tobiasbruns.authentication.user.SecurityConfiguration;
import de.tobiasbruns.authentication.user.UserManagementApplication;

/**
 * Created on 02.09.2016.
 *
 * @author Tobias Bruns
 */
@SpringBootTest(classes = { UserManagementApplication.class, SecurityConfiguration.class })
@RunWith(SpringRunner.class)
public class UserControllerTest {

	@Autowired
	private WebApplicationContext application;

	private MockMvc mockMvc;

	private static final String USER_JSON_TEMPL = "{\"username\":\"%s\",\"password\":\"%s\"}";
	private static final String THEOS_ID = "f7d1783c-734e-11e6-8b77-86f30ca893d3";
	private static final String ADMINS_ID = "571ea611-5b4d-4f4f-95d5-2239580518b5";

	@Before
	public void init() {
		mockMvc = MockMvcBuilders.webAppContextSetup(application).apply(SecurityMockMvcConfigurers.springSecurity()).build();
	}

	@Test
	public void anonymousDenied() throws Exception {
		mockMvc.perform(get("/users")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser
	public void allUsersDenied() throws Exception {
		mockMvc.perform(get("/users")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = { "ADMIN", "USER" })
	public void allUsersForAdmin() throws Exception {
		mockMvc.perform(get("/users")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "theo")
	public void allOwnData() throws Exception {
		mockMvc.perform(get("/users/f7d1783c-734e-11e6-8b77-86f30ca893d3")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "theo")
	public void readOtherUserDataDenied() throws Exception {
		mockMvc.perform(get("/users/571ea611-5b4d-4f4f-95d5-2239580518b5")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER" })
	public void userDataForAdmin() throws Exception {
		mockMvc.perform(get("/users/f7d1783c-734e-11e6-8b77-86f30ca893d3")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER" })
	public void addUser() throws Exception {
		mockMvc.perform(post("/users").content(String.format(USER_JSON_TEMPL, "newuser", "pwd")).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "theo")
	public void changOwnPassword() throws Exception {
		mockMvc.perform(patch("/users/" + THEOS_ID)//
				.content(String.format(USER_JSON_TEMPL, "theo", "newpwd")).contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	@WithMockUser(username = "theo")
	public void changeOtherUserPassword() throws Exception {
		mockMvc.perform(put("/users/" + ADMINS_ID)//
				.content(String.format(USER_JSON_TEMPL, "admin", "newpwd")).contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "theo")
	public void makeMeAdmin() throws Exception {
		mockMvc.perform(put("/users/" + THEOS_ID + "/authorities")//
				.content("http://localhost:8080/userAuthorities/ROLE_ADMIN").contentType("text/uri-list"))//
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER" })
	public void adminMakesTheoAdmin() throws Exception {
		mockMvc.perform(put("/users/" + THEOS_ID + "/authorities")//
				.content("http://localhost:8080/userAuthorities/ROLE_ADMIN").contentType("text/uri-list"))//
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER" })
	public void disabledByAdmin() throws Exception {
		mockMvc.perform(patch("/users/" + THEOS_ID)//
				.content("{\"enabled\":false}").contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().is2xxSuccessful());
	}
}
