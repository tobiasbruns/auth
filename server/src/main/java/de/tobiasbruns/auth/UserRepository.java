package de.tobiasbruns.auth;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;

import java.util.Optional;

/**
 * Created by tobias on 02.09.2016.
 */
@RepositoryRestResource(exported = true)
public interface UserRepository extends CrudRepository<User, String> {

    @Override
    @PostAuthorize("returnObject.username == authentication.name or hasRole('ADMIN')")
    User findOne(String id);

    Optional<User> findByUsername(String username);
}

