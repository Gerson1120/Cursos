package utez.edu.mx.melimas.courses.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseStudentRepository extends JpaRepository<CourseStudentEntity, Long> {
}
