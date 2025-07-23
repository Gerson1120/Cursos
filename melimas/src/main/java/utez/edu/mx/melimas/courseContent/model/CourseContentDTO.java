package utez.edu.mx.melimas.courseContent.model;

import java.util.Map;

public class CourseContentDTO {
    private Long id;
    private Long courseId;
    private Map<String, Object> content;

    public CourseContentDTO(Long id, Long courseId, Map<String, Object> content) {
        this.id = id;
        this.courseId = courseId;
        this.content = content;
    }

    public CourseContentDTO() {
    }

    public CourseContentDTO(Long courseId, Map<String, Object> content) {
        this.courseId = courseId;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }
}
