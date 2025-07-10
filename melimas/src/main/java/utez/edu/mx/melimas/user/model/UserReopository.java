package utez.edu.mx.melimas.user.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserReopository  extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String correo);
    boolean existsByEmail(String correo);


}
