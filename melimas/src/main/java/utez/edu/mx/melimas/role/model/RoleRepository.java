package utez.edu.mx.melimas.role.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Long> {

    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleName);

    boolean existsByRoleEnum(RoleEnum roleEnum);

    RoleEntity findByRoleEnum(RoleEnum roleEnum);

    Optional<RoleEntity> findByRoleEnum(String role);
}
