// src/main/java/HiyanjongTilungRai/Resume/Controller/PostController.java
package HiyanjongTilungRai.Resume.Controller;

import HiyanjongTilungRai.Resume.Model.Post;
import HiyanjongTilungRai.Resume.Services.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLConnection;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@Validated
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"}, allowCredentials = "true")
public class PostController {

    private final PostService posts;
    public PostController(PostService posts) { this.posts = posts; }

    // simple DTO that also carries a URL to the bytes
    public record ImageSummary(Long id, String name, String description, String url) {}

    @GetMapping
    public List<ImageSummary> all(HttpServletRequest req) {
        String base = ServletUriComponentsBuilder.fromRequestUri(req).replacePath(null).build().toUriString();
        return posts.all().stream()
                .map(p -> new ImageSummary(p.getId(), p.getName(), p.getDescription(),
                        base + "/api/images/" + p.getId()))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> one(@PathVariable Long id) throws IOException {
        Post p = posts.byId(id).orElseThrow(() -> new RuntimeException("not found"));
        byte[] bytes = p.getData();
        if (bytes == null || bytes.length == 0) return ResponseEntity.notFound().build();
        MediaType type = detect(bytes);
        return ResponseEntity.ok()
                .contentType(type)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + safeName(p.getName()) + "\"")
                .header("X-Description", p.getDescription() == null ? "" : p.getDescription())
                .body(bytes);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageSummary> upload(
            @RequestPart("name") @NotBlank String name,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        Post saved = posts.create(name, description, file);
        var body = new ImageSummary(saved.getId(), saved.getName(), saved.getDescription(),
                "/api/images/" + saved.getId());
        return ResponseEntity.created(URI.create("/api/images/" + saved.getId())).body(body);
    }

    // use POST for update to keep it simple
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageSummary> update(
            @PathVariable Long id,
            @RequestPart(value = "name", required = false) String name,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        Post updated = posts.update(id, name, description, file);
        var body = new ImageSummary(
                updated.getId(),
                updated.getName(),
                updated.getDescription(),
                "/api/images/" + updated.getId()
        );
        return ResponseEntity.ok(body);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        posts.delete(id);
        return ResponseEntity.noContent().build();
    }

    private static MediaType detect(byte[] bytes) throws IOException {
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
            String mime = URLConnection.guessContentTypeFromStream(b);
            return (mime != null) ? MediaType.parseMediaType(mime) : MediaType.APPLICATION_OCTET_STREAM;
        }
    }
    private static String safeName(String n) {
        if (n == null) return "file";
        return n.replaceAll("[\\r\\n\"\\\\]", "_");
    }
}
