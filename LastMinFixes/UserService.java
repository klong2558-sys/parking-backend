
package edu.sdsu.parking_backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepo repo;

    public UserService(UserRepo repo) {
        this.repo = repo;
    }

    /** Create or update a user by email (email must be non-null). */
    public void register(User user) {
        if (user == null || user.getEmail() == null) {
            log.warn("Refusing to register null user or user with null email");
            return;
        }
        // If you want to prevent duplicates by email, uncomment this guard:
        // if (repo.existsByEmail(user.getEmail())) {
        //     log.warn("Refusing to register: email {} already exists", user.getEmail());
        //     return;
        // }
        repo.save(user);
        log.info("Registered user {}", user.getEmail());
    }

    /** Delete by email, returns true if something was deleted. */
    public boolean deleteByEmail(String email) {
        if (email == null) return false;
        if (!repo.existsByEmail(email)) {
            log.warn("Delete failed: no user with email {}", email);
            return false;
        }
        repo.deleteByEmail(email);
        log.info("Deleted user {}", email);
        return true;
    }

    /** Find by email or null if not found. */
    public User findByEmail(String email) {
        return repo.findByEmail(email).orElse(null);
    }

    /** Return all users. */
    public Collection<User> findAll() {
        List<User> all = repo.findAll();
        return all;
    }
}
