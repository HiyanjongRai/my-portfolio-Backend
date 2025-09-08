// src/main/java/HiyanjongTilungRai/Resume/Repository/PostRepository.java
package HiyanjongTilungRai.Resume.Repository;

import HiyanjongTilungRai.Resume.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {}
