package de.tobiasbruns.auth;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Created on 24.08.2016.
 * 
 * @author Tobias Bruns
 */
@Service
class AuthenticationService {

	@Autowired
	private ServerTokenService tokenService;
	@Autowired
	private TbAuthTokenService coreTokenService;
	@Autowired
	private AuthenticationManager authManager;

	public TbAuthToken authenticate(UserCredentials credentials) {
		Authentication auth = authentication(credentials);
		return buildTokenString(auth);

		/*
		 * TbAuthToken token = new TbAuthToken(); UserDetails userDetails =
		 * (UserDetails) auth.getPrincipal();
		 * token.setToken(buildTokenString(auth));
		 * token.setUsername(userDetails.getUsername()); // token.setIssuedAt();
		 * return token;
		 */
	}

	private Authentication authentication(UserCredentials credentials) {
		return authManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));
	}

	private TbAuthToken buildTokenString(Authentication authentication) {
		return tokenService.buildToken((User) authentication.getPrincipal());
	}

	// String authenticateToStringToken(UserCredentials credentials) {
	// Authentication authentication = authManager.authenticate(new
	// UsernamePasswordAuthenticationToken(credentials.getUsername(),
	// credentials.getPassword()));
	// return tokenService.buildToken((UserDetails)
	// authentication.getPrincipal());
	// }
	//
	public TbAuthToken refreshToken(String oldToken) {
		Authentication authentication = authManager.authenticate(new PreAuthenticatedAuthenticationToken(getUserFromToken(oldToken).getUsername(), oldToken));

		return tokenService.buildToken((User) authentication.getPrincipal());
	}

	public User getUserFromToken(String tokenString) {
		TbAuthToken token = coreTokenService.parseToken(tokenString);
		User user = new User();
		user.setUsername(token.getUsername());
		user.setAuthorities(token.getAuthorities().stream().map(auth -> new UserAuthority(auth.getAuthority())).collect(Collectors.toSet()));
		return user;
	}

}
