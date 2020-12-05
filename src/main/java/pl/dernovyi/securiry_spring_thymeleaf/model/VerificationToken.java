package pl.dernovyi.securiry_spring_thymeleaf.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "persistent_logins")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;
    private LocalDateTime date;
    @OneToOne
    private MyUser user;
    public VerificationToken(MyUser user, String value) {
        this.value = value;
        this.user = user;
        this.date = LocalDateTime.now();
    }

    public VerificationToken() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public MyUser getMyUser() {
        return user;
    }

    public void setMyUser(MyUser MyUser) {
        this.user = user;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
