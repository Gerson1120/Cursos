package utez.edu.mx.melimas.courses.model.studentCourse;

public class CourseViewRequestDTO {
    private Long courseId;

    public CourseViewRequestDTO(Long courseId) {
        this.courseId = courseId;
    }

    public CourseViewRequestDTO() {
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
