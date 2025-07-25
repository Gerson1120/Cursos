package utez.edu.mx.melimas.user.model;

import jakarta.persistence.*;
import utez.edu.mx.melimas.courses.model.CourseStudentEntity;
import utez.edu.mx.melimas.role.model.RoleEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false,columnDefinition = "VARCHAR(30)")
    private String name;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "surname", nullable = false, length = 30)
    private String surname;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "phone", nullable = false, length = 10)
    private String phone;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "isStatusActive", nullable = false) //boolean
    private boolean isStatusActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseStudentEntity> enrolledCourses = new ArrayList<>();


    public UserEntity() {
    }

    public UserEntity(Long id, String name, String lastName, String surname, String email, String phone, String password, boolean isStatusActive, RoleEntity role, List<CourseStudentEntity> enrolledCourses) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.isStatusActive = isStatusActive;
        this.role = role;
        this.enrolledCourses = enrolledCourses;
    }

    public UserEntity(String name, String lastName, String surname, String email, String phone, String password, boolean isStatusActive, RoleEntity role, List<CourseStudentEntity> enrolledCourses) {
        this.name = name;
        this.lastName = lastName;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.isStatusActive = isStatusActive;
        this.role = role;
        this.enrolledCourses = enrolledCourses;
    }

    public UserEntity(String name, String lastName, String surname, String email, String phone, String password, boolean isStatusActive, RoleEntity role) {
        this.name = name;
        this.lastName = lastName;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.isStatusActive = isStatusActive;
        this.role = role;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getStatusActive() {
        return isStatusActive;
    }

    public void setStatusActive(boolean statusActive) {
        this.isStatusActive = statusActive;
    }

    public RoleEntity getRol() {
        return role;
    }

    public void setRoles(RoleEntity role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
