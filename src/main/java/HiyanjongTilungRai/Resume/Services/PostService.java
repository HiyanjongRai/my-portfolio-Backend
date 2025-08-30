package HiyanjongTilungRai.Resume.Services;

import HiyanjongTilungRai.Resume.Model.Post;
import HiyanjongTilungRai.Resume.Repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Save new post
    public Post savePost(String name, String description, MultipartFile file) throws IOException {
        Post post = new Post();
        post.setName(name);
        post.setDescription(description);
        post.setData(file.getBytes());
        return postRepository.save(post);
    }

    // Get all posts
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // ✅ Delete post by ID
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }
}
