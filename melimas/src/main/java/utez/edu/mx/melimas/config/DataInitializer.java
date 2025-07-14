package utez.edu.mx.melimas.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import utez.edu.mx.melimas.categories.model.CategoryEntity;
import utez.edu.mx.melimas.categories.model.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, CategoryRepository categoryRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryRepository = categoryRepository;
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
                    roleRepository.findByRoleEnum(RoleEnum.ADMIN).get()
            );
            userRepository.save(admin);
        }

        if (!userRepository.existsByEmail("student@gmail.com")) {
            UserEntity student = new UserEntity(
                    "Student", "Test", "User",
                    "student@gmail.com", passwordEncoder.encode("1234"),
                    true,
                    roleRepository.findByRoleEnum(RoleEnum.STUDENT).get()
            );
            userRepository.save(student);
        }

        if (!userRepository.existsByEmail("teacher@gmail.com")) {
            UserEntity teacher = new UserEntity(
                    "Teacher", "Test", "User",
                    "teacher@gmail.com", passwordEncoder.encode("1234"),
                    true,
                    roleRepository.findByRoleEnum(RoleEnum.TEACHER).get()
            );
            userRepository.save(teacher);
        }
        createCategoryIfNotExists("Programación", "Cursos sobre Java, Python, C++");
        createCategoryIfNotExists("Matemáticas", "Álgebra, cálculo, estadística y más");
        createCategoryIfNotExists("Idiomas", "Inglés, francés, alemán y más");
        createCategoryIfNotExists("Ciencia", "Física, química, biología");
        createCategoryIfNotExists("Negocios", "Administración, finanzas, emprendimiento");
    }
    private void createCategoryIfNotExists(String name, String description) {
        if (!categoryRepository.existsByName(name)) {
            categoryRepository.save(new CategoryEntity(name, description, true));
        }
    }
}
