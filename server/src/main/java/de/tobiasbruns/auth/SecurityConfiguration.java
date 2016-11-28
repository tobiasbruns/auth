package de.tobiasbruns.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

/**
 * Created by tobias on 02.09.2016.
 */
@Configuration
@ComponentScan(basePackages = "de.tobiasbruns")
@Import(SecurityFilterChainConfiguration.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private PreAuthenticatedAuthenticationProvider preAuthProvider;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new TBAuthUserDetailsService();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService());
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeRequests().antMatchers("/tokens").permitAll()
				// .mvcMatchers(HttpMethod.GET, "/users/{id}").authenticated()
				// .mvcMatchers("/users").hasRole("ADMIN")
				.anyRequest().denyAll();

		// http.addFilterBefore(sessionTokenFilter(),
		// RequestHeaderAuthenticationFilter.class);
	}

	// @Bean
	// @Primary
	// public PreAuthenticatedAuthenticationProvider preAuthProvider() {
	// PreAuthenticatedAuthenticationProvider provider = new
	// PreAuthenticatedAuthenticationProvider();
	// provider.setPreAuthenticatedUserDetailsService(getAuthDetailsService());
	// return provider;
	// }
	//
	// @Bean
	// @Primary
	// public
	// AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken>
	// getAuthDetailsService() {
	// return new UserDetailsByNameServiceWrapper<>(userDetailsService());
	// }

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(preAuthProvider);
		auth.authenticationProvider(daoAuthProvider());
	}

}
