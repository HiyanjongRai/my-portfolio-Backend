package HiyanjongTilungRai.Resume.Controller;

import HiyanjongTilungRai.Resume.Dto.EducationDetailsDTO;
import HiyanjongTilungRai.Resume.Model.EducationDetails;
import HiyanjongTilungRai.Resume.Model.EducationLevel;
import HiyanjongTilungRai.Resume.Repository.EducationDetailsRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/education")
@CrossOrigin
public class EducationDetailsController {

    private final EducationDetailsRepository repo;

    public EducationDetailsController(EducationDetailsRepository repo) {
        this.repo = repo;
    }

    // CREATE
    @PostMapping
    public EducationDetailsDTO create(@RequestBody EducationDetailsDTO dto) {
        EducationDetails entity = toEntity(dto);
        entity.setId(null); // make sure id is auto-generated
        return toDTO(repo.save(entity));
    }

    // READ: all
    @GetMapping
    public List<EducationDetailsDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // READ: by id
    @GetMapping("/{id}")
    public ResponseEntity<EducationDetailsDTO> getOne(@PathVariable Long id) {
        return repo.findById(id)
                .map(e -> ResponseEntity.ok(toDTO(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    // READ: by level (SCHOOL, COLLEGE, UNIVERSITY)
    @GetMapping("/level/{level}")
    public List<EducationDetailsDTO> getByLevel(@PathVariable EducationLevel level) {
        return repo.findByLevel(level).stream().map(this::toDTO).collect(Collectors.toList());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<EducationDetailsDTO> update(@PathVariable Long id, @RequestBody EducationDetailsDTO dto) {
        return repo.findById(id)
                .map(existing -> {
                    existing.setInstitutionName(dto.getInstitutionName());
                    existing.setDegree(dto.getDegree());
                    existing.setFieldOfStudy(dto.getFieldOfStudy());
                    existing.setLevel(dto.getLevel());
                    existing.setLocation(dto.getLocation());
                    existing.setStartedYear(dto.getStartedYear());
                    existing.setCompletedYear(dto.getCompletedYear());
                    existing.setGrade(dto.getGrade());
                    existing.setDescription(dto.getDescription());
                    return ResponseEntity.ok(toDTO(repo.save(existing)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return repo.findById(id)
                .map(e -> {
                    repo.delete(e);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // --- Helper methods ---
    private EducationDetailsDTO toDTO(EducationDetails e) {
        return new EducationDetailsDTO(
                e.getId(),
                e.getInstitutionName(),
                e.getDegree(),
                e.getFieldOfStudy(),
                e.getLevel(),
                e.getLocation(),
                e.getStartedYear(),
                e.getCompletedYear(),
                e.getGrade(),
                e.getDescription()
        );
    }

    private EducationDetails toEntity(EducationDetailsDTO dto) {
        return new EducationDetails(
                dto.getInstitutionName(),
                dto.getDegree(),
                dto.getFieldOfStudy(),
                dto.getLevel(),
                dto.getLocation(),
                dto.getStartedYear(),
                dto.getCompletedYear(),
                dto.getGrade(),
                dto.getDescription()
        );
    }
}
