// src/main/java/HiyanjongTilungRai/Resume/Services/PostService.java
package HiyanjongTilungRai.Resume.Services;

import HiyanjongTilungRai.Resume.Model.Post;
import HiyanjongTilungRai.Resume.Repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository repo;
    public PostService(PostRepository repo) { this.repo = repo; }

    public Post create(String name, String desc, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("file is required");
        Post p = new Post();
        p.setName(name != null ? name.trim() : null);
        p.setDescription(desc != null ? desc.trim() : null);
        p.setData(file.getBytes());
        return repo.save(p);
    }

    public Post update(Long id, String name, String desc, MultipartFile file) throws IOException {
        Post p = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        if (name != null && !name.isBlank()) p.setName(name.trim());
        if (desc != null) p.setDescription(desc.trim());
        if (file != null && !file.isEmpty()) p.setData(file.getBytes());
        return repo.save(p);
    }

    public List<Post> all() { return repo.findAll(); }
    public Optional<Post> byId(Long id) { return repo.findById(id); }
    public void delete(Long id) { repo.deleteById(id); }
}
