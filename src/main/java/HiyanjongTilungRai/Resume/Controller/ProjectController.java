package HiyanjongTilungRai.Resume.Controller;

import HiyanjongTilungRai.Resume.Model.Project;

import HiyanjongTilungRai.Resume.Services.ProjectService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // Create new project
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Project createProject(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam(required = false) String techStack,
            @RequestParam MultipartFile file,
            @RequestParam(required = false) String demoLink,
            @RequestParam(required = false) String codeLink
    ) throws IOException {
        return projectService.saveProject(title, description, techStack, file, demoLink, codeLink);
    }

    // Get all projects
    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    // Get project by ID
    @GetMapping("/{id}")
    public Project getProject(@PathVariable Long id) {
        return projectService.getProjectById(id).orElseThrow(() -> new RuntimeException("Project not found"));
    }

    // Delete project
    @DeleteMapping("/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return "Project deleted successfully";
    }

    // Fetch project image
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getProjectImage(@PathVariable Long id) {
        Project project = projectService.getProjectById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + project.getTitle() + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(project.getData());
    }
}
