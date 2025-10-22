package edu.sdsu.parking_backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // --- CRUD ---

    public User create(User user) {
        if (user == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is required");
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        User saved = userRepo.save(user);
        log.info("Created user id={} email={}", saved.getUserID(), saved.getEmail());
        return saved;
    }

    public User getById(Integer id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User getByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!userRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepo.deleteById(id);
        log.info("Deleted user id={}", id);
    }

    // --- Auth-like helpers ---

    @Transactional
    public User login(String email, String password) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!Objects.equals(user.getPassword(), password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        user.setLoggedIn(true);
        User saved = userRepo.save(user);
        log.info("User logged in email={}", email);
        return saved;
    }

    @Transactional
    public User logout(Integer userId) {
        User user = getById(userId);
        user.setLoggedIn(false);
        User saved = userRepo.save(user);
        log.info("User logged out id={}", userId);
        return saved;
    }

    @Transactional
    public User updatePassword(Integer userId, String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be empty");
        }
        User user = getById(userId);
        user.setPassword(newPassword);
        User saved = userRepo.save(user);
        log.info("Updated password for user id={}", userId);
        return saved;
    }
}

