package org.example.ceylontraditionalmedicinecenter.config;

import org.example.ceylontraditionalmedicinecenter.entity.User;
import org.example.ceylontraditionalmedicinecenter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Initializes a default ADMIN user on application startup.
 * This ensures admin credentials are available immediately for login.
 */
@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "1234";
    private static final String ADMIN_ROLE = "ROLE_ADMIN";

    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
    }

    /**
     * Initializes the admin user if it doesn't already exist.
     */
    private void initializeAdminUser() {
        try {
            // Check if admin user already exists
            var existingAdmin = userRepository.findByEmail(ADMIN_EMAIL);

            if (existingAdmin.isPresent()) {
                System.out.println("\n✓ ADMIN USER ALREADY EXISTS");
                System.out.println("  Email: " + ADMIN_EMAIL);
                printSeparator();
                return;
            }

            // Create new admin user
            User adminUser = User.builder()
                    .name("Administrator")
                    .email(ADMIN_EMAIL)
                    .contact("+1-000-000-0000")
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .role(ADMIN_ROLE)
                    .build();

            userRepository.save(adminUser);

            // Print success message with credentials
            System.out.println("\n" + "=".repeat(50));
            System.out.println("  ✓ ADMIN USER CREATED SUCCESSFULLY");
            System.out.println("=".repeat(50));
            System.out.println("  Email: " + ADMIN_EMAIL);
            System.out.println("  Password: " + ADMIN_PASSWORD);
            System.out.println("  Role: " + ADMIN_ROLE);
            System.out.println("=".repeat(50) + "\n");

        } catch (Exception e) {
            System.err.println("ERROR: Failed to initialize admin user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void printSeparator() {
        System.out.println("=".repeat(50) + "\n");
    }
}
