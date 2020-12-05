package pl.dernovyi.securiry_spring_thymeleaf.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.dernovyi.securiry_spring_thymeleaf.model.MyUser;
import pl.dernovyi.securiry_spring_thymeleaf.security.SecurityService;
import pl.dernovyi.securiry_spring_thymeleaf.service.UserService;
import pl.dernovyi.securiry_spring_thymeleaf.service.UserValidator;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class UserController {
    private UserService userService;
    private SecurityService securityService;
    private UserValidator userValidator;

    public UserController(UserService userService, SecurityService securityService, UserValidator userValidator) {
        this.userService = userService;
        this.securityService = securityService;
        this.userValidator = userValidator;
    }
    @GetMapping("/registration")
    public String registration(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("userForm", new MyUser());

        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") MyUser userForm, BindingResult bindingResult, HttpServletRequest request) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm, request);

        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/welcome";
    }
    @GetMapping("/login")
    public String login(Model model, String error, String logout) {

        if (securityService.isAuthenticated()) {

            return "redirect:/";
        }

        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");
        return "login";
    }

    @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }

    @GetMapping("/verify-token")
    public String verify(@RequestParam String token){
        userService.verifyToken(token);
        return "login";
    }

}
