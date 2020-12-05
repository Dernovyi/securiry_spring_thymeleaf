package pl.dernovyi.securiry_spring_thymeleaf.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.dernovyi.securiry_spring_thymeleaf.security.SecurityService;

import java.security.Principal;

@RestController
public class TestController {

    private SecurityService securityService;

    public TestController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping("/user")
    public String getUser(Principal principal) {
        if (securityService.isAuthenticated()) {
            return "hello user with role USER " + principal.getName();
        }
        return "registration";
    }
    @GetMapping("/admin")
    public String getAdmin(Principal principal) {
        if (securityService.isAuthenticated()) {
            return "hello user with role ADMIN " + principal.getName();
        }
        return "registration";
    }
    @GetMapping("/all")
    public  String getAll() {
        return "available to all";
    }
}
