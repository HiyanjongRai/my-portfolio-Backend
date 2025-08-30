package HiyanjongTilungRai.Resume.Dto;

import java.util.Base64;

public class ProjectDTO {

    private Long id;
    private String title;
    private String description;
    private String techStack;
    private String imageBase64;
    private String demoLink;
    private String codeLink;

    public ProjectDTO() {}

    public ProjectDTO(Long id, String title, String description, String techStack,
                      String imageBase64, String demoLink, String codeLink) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.techStack = techStack;
        this.imageBase64 = imageBase64;
        this.demoLink = demoLink;
        this.codeLink = codeLink;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTechStack() { return techStack; }
    public void setTechStack(String techStack) { this.techStack = techStack; }

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }

    public String getDemoLink() { return demoLink; }
    public void setDemoLink(String demoLink) { this.demoLink = demoLink; }

    public String getCodeLink() { return codeLink; }
    public void setCodeLink(String codeLink) { this.codeLink = codeLink; }
}
