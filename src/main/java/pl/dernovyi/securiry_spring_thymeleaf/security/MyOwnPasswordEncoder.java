package pl.dernovyi.securiry_spring_thymeleaf.security;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;



@Component
public class MyOwnPasswordEncoder implements PasswordEncoder {


    public MyOwnPasswordEncoder() {
    }

    @Override
    public String encode(CharSequence rawPassword) {
        int shift = 12;
        StringBuilder res = new StringBuilder();
        for(int x = 0; x < rawPassword.length(); x++){
            char c = (char)(rawPassword.charAt(x) + shift);
            if (c > 'z')
                res.append( (char)(rawPassword.charAt(x) - (26-shift)));
            else
                res.append((char)(rawPassword.charAt(x) + shift));
        }
        return res.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        if (encodedPassword == null || encodedPassword.length() == 0) {
            return false;
        }
        return encode(rawPassword).equals(encodedPassword);
    }
}
