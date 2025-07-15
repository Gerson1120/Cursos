package utez.edu.mx.melimas.courses.model;

import jakarta.persistence.*;
import utez.edu.mx.melimas.user.model.UserEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_students")
public class CourseStudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private UserEntity student;

    private LocalDateTime enrolledAt = LocalDateTime.now();

    public CourseStudentEntity() {
    }

    public CourseStudentEntity(Long id, CourseEntity course, UserEntity student, LocalDateTime enrolledAt) {
        this.id = id;
        this.course = course;
        this.student = student;
        this.enrolledAt = enrolledAt;
    }

    public CourseStudentEntity(CourseEntity course, UserEntity student, LocalDateTime enrolledAt) {
        this.course = course;
        this.student = student;
        this.enrolledAt = enrolledAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseEntity getCourse() {
        return course;
    }

    public void setCourse(CourseEntity course) {
        this.course = course;
    }

    public UserEntity getStudent() {
        return student;
    }

    public void setStudent(UserEntity student) {
        this.student = student;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(LocalDateTime enrolledAt) {
        this.enrolledAt = enrolledAt;
    }
}
