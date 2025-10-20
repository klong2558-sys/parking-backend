
package edu.sdsu.parkingbackend;

import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/admin")

public class AdminController {

    private final Admin admin;

    public AdminController(Admin admin) {
        this.admin = admin;
        this.admin.setAdminID("ADM001"); // prototype id
    }

    // Mock login/logout toggles the in-memory flag on the Admin bean
    @PostMapping("/login")
    public String login()  { admin.login();  return "admin logged in"; }

    @PostMapping("/logout")
    public String logout() { admin.logout(); return "admin logged out"; }

    // Admin-gated report
    @GetMapping("/report")
    public BusiestReport report(@RequestParam(defaultValue = "3") int topN) {
        return admin.generateBusiestReport(topN);
    }

    // Optional: delete a user via Admin.manageUsers("DELETE", email)
    @DeleteMapping("/users/{email}")
    public java.util.Map<String, Object> deleteUser(@PathVariable String email) {
        return java.util.Map.of("ok", admin.manageUsers("DELETE", email));
    }
}

