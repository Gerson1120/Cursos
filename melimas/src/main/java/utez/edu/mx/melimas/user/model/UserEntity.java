package utez.edu.mx.melimas.user.model;

import jakarta.persistence.*;
import utez.edu.mx.melimas.role.model.RoleEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id ;


    @Column(name = "name", nullable = false,columnDefinition = "VARCHAR(30)")
    private String name;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "surname", nullable = false, length = 30)
    private String surname;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "isStatusActive", nullable = false) //boolean
    private boolean isStatusActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;


    public UserEntity() {
    }

    public UserEntity(Long user_id, String name, String lastName, String surname, String email, String password, boolean status, RoleEntity role) {
        this.user_id = user_id;
        this.name = name;
        this.lastName = lastName;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.isStatusActive = status;
        this.role = role;
    }

    public UserEntity(String name, String lastName, String surname, String email, String password, boolean status, RoleEntity role) {
        this.name = name;
        this.lastName = lastName;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.isStatusActive = status;
        this.role = role;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
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
}
