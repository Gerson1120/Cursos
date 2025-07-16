package utez.edu.mx.melimas.courses.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseStudentRepository extends JpaRepository<CourseStudentEntity, Long> {
    Optional<CourseStudentEntity> findByCourseIdAndStudent_Id(Long courseId, Long studentId);

}
