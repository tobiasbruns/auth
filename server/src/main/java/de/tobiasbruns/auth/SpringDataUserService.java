package de.tobiasbruns.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by U109359 on 02.09.2016.
 */
@Service
class SpringDataUserService implements UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public User createUser(String username, String password) {
        User newUser = new User(username, encoder.encode(password));
        return repository.save(newUser);
    }

    @Override
    public User getUser(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Cannot find User: " + username));
    }

    @Override
    @Transactional
    public void enableUser(String userId) {
        repository.findOne(userId).setEnabled(true);
    }

    @Override
    @Transactional
    public void disableUser(String userId) {
        repository.findOne(userId).setEnabled(false);
    }
}
