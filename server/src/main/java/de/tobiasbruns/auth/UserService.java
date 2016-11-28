package de.tobiasbruns.auth;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by U109359 on 02.09.2016.
 */
public interface UserService {

    User createUser(String username, String password);

    User getUser(String username) throws UsernameNotFoundException;

    void enableUser(String userId);

    void disableUser(String userId);
}
