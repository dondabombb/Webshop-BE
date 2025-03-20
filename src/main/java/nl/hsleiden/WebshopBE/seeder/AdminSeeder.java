package nl.hsleiden.WebshopBE.seeder;

import nl.hsleiden.WebshopBE.DAO.UserDAO;
import nl.hsleiden.WebshopBE.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminSeeder(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        List<UserModel> users = userDAO.getAllUsers();
        if (users == null || users.isEmpty()) {
            UserModel adminUser = new UserModel("John", "", "Doe", "admin@example.com", passwordEncoder.encode("password123"), "AMDMIN");
            adminUser.setAdmin(true);
            userDAO.createUser(adminUser);
            System.out.println("Admin user created successfully!");
        } else {
            System.out.println("Admin user already exists in the database.");
        }
    }
}
