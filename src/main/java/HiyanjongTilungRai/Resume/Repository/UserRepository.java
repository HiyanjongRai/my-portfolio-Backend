package HiyanjongTilungRai.Resume.Repository;

import HiyanjongTilungRai.Resume.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserNameIgnoreCaseOrEmailIgnoreCase(String userName, String email);
}
