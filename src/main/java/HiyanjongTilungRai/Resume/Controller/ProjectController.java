// src/main/java/HiyanjongTilungRai/Resume/Controller/ProjectController.java
package HiyanjongTilungRai.Resume.Controller;

import HiyanjongTilungRai.Resume.Model.Project;
import HiyanjongTilungRai.Resume.Services.ProjectService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLConnection;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    public ProjectController(ProjectService projectService) { this.projectService = projectService; }

    // ===== READ (public) =====
    @GetMapping
    public List<Project> getAllProjects() { return projectService.getAllProjects(); }

    @GetMapping("/{id}")
    public Project getProject(@PathVariable Long id) {
        return projectService.getProjectById(id).orElseThrow(() -> new RuntimeException("Project not found"));
    }

    // ===== CREATE (ADMIN) =====
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Project> createProject(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam(required = false) String techStack,
            @RequestParam MultipartFile file,
            @RequestParam(required = false) String demoLink,
            @RequestParam(required = false) String codeLink
    ) throws IOException {
        Project saved = projectService.saveProject(title, description, techStack, file, demoLink, codeLink);
        return ResponseEntity.created(URI.create("/api/projects/" + saved.getId())).body(saved);
    }

    // ===== UPDATE (ADMIN) =====
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Project updateProject(
            @PathVariable Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String techStack,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) String demoLink,
            @RequestParam(required = false) String codeLink
    ) throws IOException {
        return projectService.updateProject(id, title, description, techStack, file, demoLink, codeLink);
    }

    // ===== DELETE (ADMIN) =====
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    // ===== IMAGE BYTES (public) =====
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getProjectImage(@PathVariable Long id) {
        Project project = projectService.getProjectById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        MediaType type = detect(project.getData());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + safeName(project.getTitle()) + "\"")
                .contentType(type)
                .body(project.getData());
    }

    /* helpers */
    private static MediaType detect(byte[] bytes) {
        if (bytes == null) return MediaType.APPLICATION_OCTET_STREAM;
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
            String mime = URLConnection.guessContentTypeFromStream(b);
            return (mime != null) ? MediaType.parseMediaType(mime) : MediaType.APPLICATION_OCTET_STREAM;
        } catch (IOException e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
    private static String safeName(String n) {
        if (n == null) return "file";
        return n.replaceAll("[\\r\\n\"\\\\]", "_");
    }
}
