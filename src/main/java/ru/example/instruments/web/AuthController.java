package ru.example.instruments.web;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.example.instruments.model.RegistrationForm;
import ru.example.instruments.service.UserService;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("form") RegistrationForm form,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            String username = form.getUsername().trim();

            if (!form.getPassword().equals(form.getConfirmPassword())) {
                bindingResult.rejectValue("confirmPassword", "password.mismatch",
                        "Пароли не совпадают");
            } else if (userService.exists(username)) {
                bindingResult.rejectValue("username", "username.exists",
                        "Пользователь с таким логином уже существует");
            } else {
                userService.register(username, form.getPassword());
                redirectAttributes.addFlashAttribute("message",
                        "Регистрация завершена. Теперь войдите в систему.");
                return "redirect:/login";
            }
        }
        return "register";
    }
}
