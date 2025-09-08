// src/main/java/HiyanjongTilungRai/Resume/Services/ProjectService.java
package HiyanjongTilungRai.Resume.Services;

import HiyanjongTilungRai.Resume.Model.Project;
import HiyanjongTilungRai.Resume.Repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project saveProject(String title, String description, String techStack,
                               MultipartFile file, String demoLink, String codeLink) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Project image file is required");
        }
        Project project = new Project();
        project.setTitle(title != null ? title.trim() : null);
        project.setDescription(description != null ? description.trim() : null);
        project.setTechStack(techStack != null ? techStack.trim() : null);
        project.setData(file.getBytes());
        project.setDemoLink(demoLink != null ? demoLink.trim() : null);
        project.setCodeLink(codeLink != null ? codeLink.trim() : null);

        return projectRepository.save(project);
    }

    public Project updateProject(Long id, String title, String description, String techStack,
                                 MultipartFile file, String demoLink, String codeLink) throws IOException {
        Project p = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));

        if (title != null && !title.isBlank()) p.setTitle(title.trim());
        if (description != null)              p.setDescription(description.trim());
        if (techStack != null)                p.setTechStack(techStack.trim());
        if (demoLink != null)                 p.setDemoLink(demoLink.trim());
        if (codeLink != null)                 p.setCodeLink(codeLink.trim());
        if (file != null && !file.isEmpty())  p.setData(file.getBytes());

        return projectRepository.save(p);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}
