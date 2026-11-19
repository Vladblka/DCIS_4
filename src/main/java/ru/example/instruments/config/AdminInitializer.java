package ru.example.instruments.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.example.instruments.model.AppUser;
import ru.example.instruments.repository.UserRepository;

@Component
public class AdminInitializer implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(AdminInitializer.class);

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!userRepository.existsByUsername(ADMIN_USERNAME)) {
            userRepository.save(
                    ADMIN_USERNAME,
                    passwordEncoder.encode(ADMIN_PASSWORD),
                    AppUser.ROLE_ADMIN);
            log.info("Создан администратор по умолчанию: {}", ADMIN_USERNAME);
        }
    }
}
