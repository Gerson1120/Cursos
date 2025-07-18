package utez.edu.mx.melimas.courses.model;

import jakarta.persistence.*;
import utez.edu.mx.melimas.categories.model.CategoryEntity;
import utez.edu.mx.melimas.user.model.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String description;

    private String syllabus;

    private int duration;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private boolean enabled = true;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private UserEntity teacher;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseStudentEntity> enrollments = new ArrayList<>();

    public CourseEntity() {
    }

    public CourseEntity(Long id, String name, String description, String syllabus, int duration, String imageUrl, boolean enabled, CategoryEntity category, UserEntity teacher, List<CourseStudentEntity> enrollments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.syllabus = syllabus;
        this.duration = duration;
        this.imageUrl = imageUrl;
        this.enabled = enabled;
        this.category = category;
        this.teacher = teacher;
        this.enrollments = enrollments;
    }

    public CourseEntity(String name, String description, String syllabus, int duration, String imageUrl, boolean enabled, CategoryEntity category, UserEntity teacher, List<CourseStudentEntity> enrollments) {
        this.name = name;
        this.description = description;
        this.syllabus = syllabus;
        this.duration = duration;
        this.imageUrl = imageUrl;
        this.enabled = enabled;
        this.category = category;
        this.teacher = teacher;
        this.enrollments = enrollments;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public UserEntity getTeacher() {
        return teacher;
    }

    public void setTeacher(UserEntity teacher) {
        this.teacher = teacher;
    }

    public List<CourseStudentEntity> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<CourseStudentEntity> enrollments) {
        this.enrollments = enrollments;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }
}
