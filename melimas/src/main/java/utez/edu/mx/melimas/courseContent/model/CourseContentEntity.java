package utez.edu.mx.melimas.courseContent.model;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;
import utez.edu.mx.melimas.courses.model.CourseEntity;

import java.util.Map;

@Entity
@Table(name = "course_contents")
public class CourseContentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;


    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> contentJson;

    public CourseContentEntity(Long id, CourseEntity course, Map<String, Object> contentJson) {
        this.id = id;
        this.course = course;
        this.contentJson = contentJson;
    }

    public CourseContentEntity(Map<String, Object> contentJson, CourseEntity course) {
        this.contentJson = contentJson;
        this.course = course;
    }

    public CourseContentEntity() {
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

    public Map<String, Object> getContentJson() {
        return contentJson;
    }

    public void setContentJson(Map<String, Object> contentJson) {
        this.contentJson = contentJson;
    }
}
