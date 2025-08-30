package HiyanjongTilungRai.Resume.Repository;

import HiyanjongTilungRai.Resume.Model.EducationDetails;
import HiyanjongTilungRai.Resume.Model.EducationLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EducationDetailsRepository extends JpaRepository<EducationDetails, Long> {

    // Optional: Get all education records by level (SCHOOL, COLLEGE, UNIVERSITY)
    List<EducationDetails> findByLevel(EducationLevel level);

}
