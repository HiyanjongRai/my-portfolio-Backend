package HiyanjongTilungRai.Resume.Repository;

import HiyanjongTilungRai.Resume.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    // Corrected method name
    User findByUserName(String userName);

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);
}
