package ru.example.instruments.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Вход, регистрация и оформление доступны всем
                        .requestMatchers("/login", "/register", "/css/**", "/error").permitAll()
                        // Просмотр записей и поиск — и пользователю, и администратору
                        .requestMatchers(HttpMethod.GET, "/instruments", "/instruments/search")
                        .hasAnyRole("USER", "ADMIN")
                        // Добавление, изменение и удаление — только администратору
                        .requestMatchers("/instruments/**").hasRole("ADMIN")
                        // Остальные страницы (включая главную) — только после входа
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());
        return http.build();
    }
}
