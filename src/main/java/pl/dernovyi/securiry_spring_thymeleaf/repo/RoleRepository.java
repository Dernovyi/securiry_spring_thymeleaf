package pl.dernovyi.securiry_spring_thymeleaf.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dernovyi.securiry_spring_thymeleaf.model.Role;

import java.util.stream.Stream;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String nameRole);
}
