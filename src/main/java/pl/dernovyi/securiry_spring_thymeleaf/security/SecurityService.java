package pl.dernovyi.securiry_spring_thymeleaf.security;

public interface SecurityService {
    boolean isAuthenticated();
    void autoLogin(String username, String password);
}
