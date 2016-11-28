package de.tobiasbruns.authentication.user;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonProperty;

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

	@Override
	public String getAuthority() {
		return authority;
	}

	@JsonProperty
	public String getAuthorityName() {
		return authority;
	}

}
