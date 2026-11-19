package ru.example.instruments.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.instruments.model.AppUser;
import ru.example.instruments.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /** Spring Security вызывает этот метод при входе пользователя. */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    /** Регистрирует нового пользователя с ролью USER и хеширует пароль. */
    @Transactional
    public void register(String username, String rawPassword) {
        repository.save(username, passwordEncoder.encode(rawPassword), AppUser.ROLE_USER);
    }

    public boolean exists(String username) {
        return repository.existsByUsername(username);
    }
}
