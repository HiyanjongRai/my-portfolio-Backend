package HiyanjongTilungRai.Resume.Controller;

import HiyanjongTilungRai.Resume.Model.Post;
import HiyanjongTilungRai.Resume.Services.PostService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")  // Matches your SecurityConfig
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // Upload image with description
    @PostMapping("/upload")
    @PreAuthorize("hasRole('Admin')")
    public Post uploadPost(@RequestParam("name") String name,
                           @RequestParam("description") String description,
                           @RequestParam("file") MultipartFile file) throws IOException {
        return postService.savePost(name, description, file);
    }

    // Get all posts (metadata only: id, name, description)
    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    // Get image by id
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Post post = postService.getAllPosts().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + post.getName() + "\"")
                .header("X-Description", post.getDescription())  // Exposed via CORS
                .contentType(MediaType.IMAGE_JPEG)  // adjust if needed
                .body(post.getData());
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "Post deleted successfully";
    }
}
