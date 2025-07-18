package utez.edu.mx.melimas.user.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.melimas.role.model.RoleEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String correo);
    boolean existsByEmail(String correo);

    List<UserEntity> findAllByRole(RoleEntity roleEntity);
}
