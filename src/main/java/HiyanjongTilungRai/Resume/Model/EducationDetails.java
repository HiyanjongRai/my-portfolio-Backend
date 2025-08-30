package HiyanjongTilungRai.Resume.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "education_details")
public class EducationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String institutionName;   // School/College/University name
    private String degree;            // e.g., SEE, +2, BCA, MBA
    private String fieldOfStudy;      // e.g., Computer Science, Management

    @Enumerated(EnumType.STRING)
    private EducationLevel level;     // SCHOOL / COLLEGE / UNIVERSITY

    private String location;          // Kathmandu, Lalitpur, etc.
    private Integer startedYear;      // e.g., 2017
    private Integer completedYear;    // e.g., 2021

    private String grade;             // GPA, Percentage, Division
    private String description;       // Notes / Achievements

    // ✅ Constructors
    public EducationDetails() {}

    public EducationDetails(String institutionName, String degree, String fieldOfStudy,
                            EducationLevel level, String location, Integer startedYear,
                            Integer completedYear, String grade, String description) {
        this.institutionName = institutionName;
        this.degree = degree;
        this.fieldOfStudy = fieldOfStudy;
        this.level = level;
        this.location = location;
        this.startedYear = startedYear;
        this.completedYear = completedYear;
        this.grade = grade;
        this.description = description;
    }

    // ✅ Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getInstitutionName() { return institutionName; }
    public void setInstitutionName(String institutionName) { this.institutionName = institutionName; }

    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }

    public String getFieldOfStudy() { return fieldOfStudy; }
    public void setFieldOfStudy(String fieldOfStudy) { this.fieldOfStudy = fieldOfStudy; }

    public EducationLevel getLevel() { return level; }
    public void setLevel(EducationLevel level) { this.level = level; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getStartedYear() { return startedYear; }
    public void setStartedYear(Integer startedYear) { this.startedYear = startedYear; }

    public Integer getCompletedYear() { return completedYear; }
    public void setCompletedYear(Integer completedYear) { this.completedYear = completedYear; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
