package utez.edu.mx.melimas.user.model;

public class UserDTO {
    private Long id;
    private String name;
    private String lastName;
    private String surname;
    private String email;
    private String password;
    private String role;

    public UserDTO(String name, String lastName, String surname, String email, String password, String role) {
        this.name = name;
        this.lastName = lastName;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UserDTO(Long id, String name, String lastName, String surname, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.surname = surname;
        this.email = email;
        this.password = password;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

