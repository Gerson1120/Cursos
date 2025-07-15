package utez.edu.mx.melimas.courses.model;


public class CourseDTO {
    private String name;
    private String description;
    private String syllabus;
    private int duration;
    private String imageUrl;
    private Long categoryId;
    private Long teacherId;

    public CourseDTO() {
    }

    public CourseDTO(String name, String description, String syllabus, int duration, String imageUrl, Long categoryId, Long teacherId) {
        this.name = name;
        this.description = description;
        this.syllabus = syllabus;
        this.duration = duration;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.teacherId = teacherId;
    }

    public CourseDTO(String description, String syllabus, int duration, String imageUrl, Long categoryId, Long teacherId) {
        this.description = description;
        this.syllabus = syllabus;
        this.duration = duration;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
}
