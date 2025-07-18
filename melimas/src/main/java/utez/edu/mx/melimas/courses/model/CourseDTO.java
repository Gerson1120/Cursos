package utez.edu.mx.melimas.courses.model;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CourseDTO {
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 \\-]{3,100}$",
            message = "El nombre debe tener entre 3 y 100 caracteres y solo puede contener letras, números, espacios y guiones."
    )
    private String name;
    @Size(max = 255, message = "La descripción no debe exceder los 255 caracteres.")
    private String description;
    @Size(max = 500, message = "El temario no debe exceder los 500 caracteres.")
    private String syllabus;
    @Min(value = 1, message = "La duración debe ser mayor a 0.")
    private int duration;
    @Pattern(
            regexp = "^(https?://)?[\\w./%-]+\\.(jpg|jpeg|png)$",
            message = "La URL de la imagen debe ser válida y terminar en .jpg, .jpeg, .png "
    )
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
