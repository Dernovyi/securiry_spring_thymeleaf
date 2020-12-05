package pl.dernovyi.securiry_spring_thymeleaf.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import pl.dernovyi.securiry_spring_thymeleaf.model.MyUser;

@Repository
public interface UserRepository extends JpaRepository<MyUser, Long> {
    MyUser findByUsername(String username);
}
