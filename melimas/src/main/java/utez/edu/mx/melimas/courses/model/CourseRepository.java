package utez.edu.mx.melimas.courses.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    @Query("SELECT new utez.edu.mx.melimas.courses.model.CourseSimpleDTO(" +
            "c.id, c.name, c.description, c.imageUrl, c.enabled, " +
            "cat.id, cat.name, t.user_id, CONCAT(t.name, ' ', t.lastName)) " +
            "FROM CourseEntity c " +
            "JOIN c.teacher t " +
            "LEFT JOIN c.category cat " +
            "WHERE c.enabled = true")
    List<CourseSimpleDTO> findAllSummary();
}
