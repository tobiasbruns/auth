package de.tobiasbruns.auth;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created on 25.08.2016.
 * 
 * @author Tobias Bruns
 */
@Entity
public class UserAuthority implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@Id
	private String authority;

	UserAuthority() {
	}

	UserAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public String getAuthority() {
		return authority;
	}
}
