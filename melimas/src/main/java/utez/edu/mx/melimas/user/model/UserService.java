package utez.edu.mx.melimas.user.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.melimas.role.model.RoleEntity;
import utez.edu.mx.melimas.role.model.RoleEnum;
import utez.edu.mx.melimas.role.model.RoleRepository;
import utez.edu.mx.melimas.utils.Message;
import utez.edu.mx.melimas.utils.TypesResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository repository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> saveUserWithRole(UserDTO dto,String roleName){
        if (!dto.getRole().equalsIgnoreCase("STUDENT") && !dto.getRole().equalsIgnoreCase("TEACHER")){
            return new ResponseEntity<>(new Message("No se permite crear usuarios ADMIN", null, TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        if (repository.existsByEmail(dto.getEmail())) {
            return new ResponseEntity<>(new Message("Correo ya registrado", null, TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        RoleEnum roleEnum = RoleEnum.valueOf(roleName);

        Optional<RoleEntity> role = roleRepository.findByRoleEnum(roleEnum);
        if (role.isEmpty() || !role.isPresent()) {
            return new ResponseEntity<>(new Message("Rol inválido", null, TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity();
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setStatusActive(true);
        user.setRoles(role.get());

        user = repository.saveAndFlush(user);
        if (user == null) {
            log.info("El usuario no se registro ");
            return new ResponseEntity<>(new Message("El usuario no se registró", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        log.info("El Registro exitoso");

        return new ResponseEntity<>(new Message("Usuario creado", user, TypesResponse.SUCCESS), HttpStatus.OK);

    }

    public ResponseEntity<Message> findAll(){
        return new ResponseEntity<>(new Message("Listado de usuarios",repository.findAll(),TypesResponse.SUCCESS),HttpStatus.OK);
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<Message> updateUser(Long id, UserDTO dto) {
        Optional<UserEntity> optionalUser = repository.findById(id);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(new Message("Usuario no encontrado", TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }

        UserEntity user = optionalUser.get();
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        user = repository.saveAndFlush(user);
        log.info("Usuario con ID {} actualizado", id);

        return new ResponseEntity<>(new Message("Usuario actualizado correctamente", user, TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<Message> logicalDeleteUser(Long id) {
        Optional<UserEntity> optionalUser = repository.findById(id);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(new Message("Usuario no encontrado", TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }
        UserEntity user = optionalUser.get();
        user.setStatusActive(!user.getStatusActive());
        repository.saveAndFlush(user);
        log.info("Usuario con ID {} dado de baja (borrado lógico)", id);

        return new ResponseEntity<>(new Message("Usuario desactivado correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    public ResponseEntity<Message> getUserProfileByEmail(String email) {
        Optional<UserEntity> optionalUser = repository.findByEmail(email);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            UserEntity clone = new UserEntity();
            clone.setId(user.getId());
            clone.setName(user.getName());
            clone.setLastName(user.getLastName());
            clone.setSurname(user.getSurname());
            clone.setEmail(user.getEmail());
            clone.setPhone(user.getPhone());
            clone.setStatusActive(user.getStatusActive());
            clone.setRoles(user.getRol());

            return ResponseEntity.ok(new Message("Perfil encontrado", clone, TypesResponse.SUCCESS));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Message("Usuario no encontrado", null, TypesResponse.WARNING));
        }
    }
    @Transactional
    public ResponseEntity<Message> updatePassword(String email, String newPassword) {
        Optional<UserEntity> optionalUser = repository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(new Message("Usuario no encontrado", null, TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }

        UserEntity user = optionalUser.get();
        String hashed = passwordEncoder.encode(newPassword);
        user.setPassword(hashed);
        repository.save(user);

        return new ResponseEntity<>(new Message("Contraseña actualizada", null, TypesResponse.SUCCESS), HttpStatus.OK);
    }


    public ResponseEntity<Message> findByRol(String rol){
        RoleEnum roleEnum = RoleEnum.valueOf(rol);

        Optional<RoleEntity> role = roleRepository.findByRoleEnum(roleEnum);
        if (role.isEmpty() || !role.isPresent()) {
            return new ResponseEntity<>(new Message("Rol inválido", null, TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        List<UserEntity> userEntityList = repository.findAllByRole(role.get());
        return  new ResponseEntity<>(new Message("Usuarios por rol encontrados",userEntityList,TypesResponse.SUCCESS),HttpStatus.OK);

    }

    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // Este es el "sub" del token (correo)

        Optional<UserEntity> userOpt = repository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
        }

        return userOpt.get().getId();
    }
    public String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No se encontró usuario autenticado");
        }

        String email = authentication.getName();
        UserEntity user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return user.getRol().getRoleEnum().name();
    }
}
