package de.tobiasbruns.auth;

import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created on 19.09.2016.
 * 
 * @author Tobias Bruns
 */
public class TbAuthAuthority implements GrantedAuthority {

	private static final long serialVersionUID = 1L;
	private String authority;

	TbAuthAuthority() {

	}

	public TbAuthAuthority(String authority) {
		Objects.requireNonNull(authority);
		this.authority = authority;
	}

	@Override
	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TbAuthAuthority that = (TbAuthAuthority) o;

		return authority.equals(that.authority);
	}

	@Override
	public int hashCode() {
		return authority.hashCode();
	}
}
