package utez.edu.mx.melimas.courseContent.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseContentRepository extends JpaRepository<CourseContentEntity, Long> {
    Optional<CourseContentEntity> findByCourseId(Long courseId);
}
