package pl.dernovyi.securiry_spring_thymeleaf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.dernovyi.securiry_spring_thymeleaf.model.MyUser;
import pl.dernovyi.securiry_spring_thymeleaf.model.VerificationToken;
import pl.dernovyi.securiry_spring_thymeleaf.repo.RoleRepository;
import pl.dernovyi.securiry_spring_thymeleaf.repo.UserRepository;
import pl.dernovyi.securiry_spring_thymeleaf.repo.VerificationTokenRepo;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@Service
@Configuration
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepo verificationTokenRepo;
    private final MailSenderService mailSenderService;
    @Bean
    public PasswordEncoder getPasswordEncoder(){

        return new BCryptPasswordEncoder();
    }

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, VerificationTokenRepo verificationTokenRepo, MailSenderService mailSenderService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.verificationTokenRepo = verificationTokenRepo;
        this.mailSenderService = mailSenderService;
    }

    @Override
    public void save(MyUser user, HttpServletRequest request) {
        if(!user.isRole()){
            user.setPassword(getPasswordEncoder().encode(user.getPassword()));
            user.addRoles(roleRepository.findByName("ROLE_USER"));
            user.setEnabled(false);
            userRepository.save(user);
            mailSender(user, request);
        }else {
            user.setPassword(getPasswordEncoder().encode(user.getPassword()));
            user.addRoles(roleRepository.findByName("ROLE_ADMIN"));
            user.setEnabled(false);
            userRepository.save(user);
            mailSender(user, request);
        }

    }

    private void mailSender(MyUser user, HttpServletRequest request) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepo.save(verificationToken);
        String url = "http://" + request.getServerName() +
                ":" +
                request.getServerPort()+
                request.getContextPath() +
                "/verify-token?token="+ token;
        try {
            mailSenderService.sendMail(user.getUsername(), "VerificationToken", url  , false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MyUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void verifyToken(String token) {
        VerificationToken verificationToken = verificationTokenRepo.findByValue(token);
        LocalDateTime time = LocalDateTime.now().plusMinutes(1);
        LocalDateTime createTime = verificationToken.getDate();
        MyUser myUser = verificationToken.getMyUser();
        if(time.isAfter(createTime)){
            verificationTokenRepo.delete(verificationToken);
        }else{
            myUser.setEnabled(true);
            verificationTokenRepo.delete(verificationToken);
            userRepository.save(myUser);
        }


    }
}
