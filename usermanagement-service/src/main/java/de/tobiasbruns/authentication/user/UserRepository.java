package de.tobiasbruns.authentication.user;

import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Created on 23.09.2016
 *
 * @author Tobias Bruns
 */
@RepositoryRestResource(excerptProjection = NoFlagsProjection.class)
public interface UserRepository extends Repository<User, String> {

	@PostAuthorize("returnObject == null or returnObject.username == authentication.name or hasRole('ADMIN')")
	User findOne(String id);

	@PreAuthorize("hasRole('ADMIN')")
	Collection<User> findAll();

	// @PreAuthorize("hasRole('ADMIN')")
	User save(@Param("user") User user);

	@Modifying
	@Query("UPDATE User u SET u.password=:newPassword WHERE u.userId=:id")
	void changePassword(@Param("id") String id, @Param("newPassword") String newPassword);
}
