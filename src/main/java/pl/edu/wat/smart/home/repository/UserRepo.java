package pl.edu.wat.smart.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.wat.smart.home.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);
}
