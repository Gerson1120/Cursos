package utez.edu.mx.melimas.courses.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utez.edu.mx.melimas.user.model.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseStudentRepository extends JpaRepository<CourseStudentEntity, Long> {
    Optional<CourseStudentEntity> findByCourseIdAndStudent_Id(Long courseId, Long studentId);

    Optional<CourseStudentEntity> findByCourseIdAndStudentId(Long courseId, Long id);

    @Query("SELECT new utez.edu.mx.melimas.courses.model.CourseSimpleDTO(" +
            "c.id, c.name, c.description, c.imageUrl, c.enabled, " +
            "cat.id, cat.name, t.id, CONCAT(t.name, ' ', t.lastName)) " +
            "FROM CourseStudentEntity cs " +
            "JOIN cs.course c " +
            "JOIN c.teacher t " +
            "LEFT JOIN c.category cat " +
            "WHERE cs.student.email = :email AND c.enabled = true")
    List<CourseSimpleDTO> findCoursesByStudentEmail(@Param("email") String email);

    boolean existsByCourseIdAndStudentId(Long courseId, Long userId);
}
