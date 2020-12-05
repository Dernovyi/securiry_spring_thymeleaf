package pl.dernovyi.securiry_spring_thymeleaf.service;

import pl.dernovyi.securiry_spring_thymeleaf.model.MyUser;


import javax.servlet.http.HttpServletRequest;

public interface UserService {
    void save(MyUser user, HttpServletRequest http);

    MyUser findByUsername(String username);

    void verifyToken(String token);
}
