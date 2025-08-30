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
        Project project = new Project();
        project.setTitle(title);
        project.setDescription(description);
        project.setTechStack(techStack);
        project.setData(file.getBytes());
        project.setDemoLink(demoLink);
        project.setCodeLink(codeLink);

        return projectRepository.save(project);
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
