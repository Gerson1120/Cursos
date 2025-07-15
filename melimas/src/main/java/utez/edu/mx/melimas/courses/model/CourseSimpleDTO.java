package utez.edu.mx.melimas.courses.model;

public class CourseSimpleDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private boolean enabled;
    private Long categoryId;
    private String categoryName;
    private Long teacherId;
    private String teacherName;

    public CourseSimpleDTO() {
    }

    public CourseSimpleDTO(Long id, String name, String description, String imageUrl, boolean enabled, Long categoryId, String categoryName, Long teacherId, String teacherName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.enabled = enabled;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
    }

    public CourseSimpleDTO(String name, String description, String imageUrl, boolean enabled, Long categoryId, String categoryName, Long teacherId, String teacherName) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.enabled = enabled;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

