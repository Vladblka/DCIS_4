package ru.example.instruments.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistrationForm {

    @NotBlank(message = "Укажите логин")
    @Size(min = 3, max = 60, message = "Логин должен содержать от 3 до 60 символов")
    @Pattern(regexp = "[A-Za-z0-9_]+", message = "Логин может содержать только латинские буквы, цифры и _")
    private String username;

    @NotBlank(message = "Укажите пароль")
    @Size(min = 4, max = 60, message = "Пароль должен содержать от 4 до 60 символов")
    private String password;

    @NotBlank(message = "Повторите пароль")
    private String confirmPassword;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
