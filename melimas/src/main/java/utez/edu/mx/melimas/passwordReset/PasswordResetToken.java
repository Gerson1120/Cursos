package utez.edu.mx.melimas.passwordReset;

import jakarta.persistence.*;
import utez.edu.mx.melimas.user.model.UserEntity;

import java.time.LocalDateTime;

@Entity
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime expirationDate;

    @ManyToOne
    private UserEntity user;

    public PasswordResetToken() {
    }

    public PasswordResetToken(Long id, String token, LocalDateTime expirationDate, UserEntity user) {
        this.id = id;
        this.token = token;
        this.expirationDate = expirationDate;
        this.user = user;
    }

    public PasswordResetToken(String token, LocalDateTime expirationDate, UserEntity user) {
        this.token = token;
        this.expirationDate = expirationDate;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}