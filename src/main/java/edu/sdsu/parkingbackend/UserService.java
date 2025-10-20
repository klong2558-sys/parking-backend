
package edu.sdsu.parkingbackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

        private static final Logger log = LoggerFactory.getLogger(UserService.class);

        // Prototype: key by userEmail (unique per user)
        private static final Map<String, User> usersByEmail = new ConcurrentHashMap<>();

        public void register(User user) {
            if (user == null || user.userEmail == null) {
                log.warn("Refusing to register null user or user with null email");
                return;
            }
            usersByEmail.put(user.userEmail, user);
            log.info("Registered user {}", user.userEmail);
        }

        public static boolean deleteByEmail(String email) {
            if (email == null) return false;
            User removed = usersByEmail.remove(email);
            if (removed == null) {
                log.warn("Delete failed: no user with email {}", email);
                return false;
            }
            log.info("Deleted user {}", email);
            return true;
        }

        public User findByEmail(String email) {
            return usersByEmail.get(email);
        }

        public Collection<User> findAll() {
            return usersByEmail.values();
        }
}

