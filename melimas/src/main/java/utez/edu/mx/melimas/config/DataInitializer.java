package utez.edu.mx.melimas.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import utez.edu.mx.melimas.role.model.RoleEntity;
import utez.edu.mx.melimas.role.model.RoleEnum;
import utez.edu.mx.melimas.role.model.RoleRepository;
import utez.edu.mx.melimas.user.model.UserEntity;
import utez.edu.mx.melimas.user.model.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner{



    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        System.err.println(">>> DataInitializer ejecutándose...");
        // Crear roles si no existen
        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (!roleRepository.existsByRoleEnum(roleEnum)) {
                RoleEntity role = new RoleEntity();
                role.setRoleEnum(roleEnum);
                roleRepository.save(role);
            }
        }

        // Crear usuarios de prueba si no existen
        if (!userRepository.existsByEmail("admin@gmail.com")) {
            UserEntity admin = new UserEntity(
                    "Admin", "Test", "User",
                    "admin@gmail.com", passwordEncoder.encode("1234"),
                    true,
                    roleRepository.findByRoleEnum(RoleEnum.ADMIN)
            );
            userRepository.save(admin);
        }

        if (!userRepository.existsByEmail("student@gmail.com")) {
            UserEntity student = new UserEntity(
                    "Student", "Test", "User",
                    "student@gmail.com", passwordEncoder.encode("1234"),
                    true,
                    roleRepository.findByRoleEnum(RoleEnum.STUDENT)
            );
            userRepository.save(student);
        }

        if (!userRepository.existsByEmail("teacher@gmail.com")) {
            UserEntity teacher = new UserEntity(
                    "Teacher", "Test", "User",
                    "teacher@gmail.com", passwordEncoder.encode("1234"),
                    true,
                    roleRepository.findByRoleEnum(RoleEnum.TEACHER)
            );
            userRepository.save(teacher);
        }
    }
}
