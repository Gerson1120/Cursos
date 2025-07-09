package utez.edu.mx.melimas.user.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id ;


    @Column(name = "name", nullable = false,columnDefinition = "VARCHAR(30)")
    private String name;

    @Column(name = "last_name", nullable = false, length = 255)
    private String lastName;

    @Column(name = "surname", nullable = false, length = 255)
    private String surname;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "status", nullable = false, length = 255) //boolean
    private String status;
}
