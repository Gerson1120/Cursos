package utez.edu.mx.melimas.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import utez.edu.mx.melimas.categories.model.CategoryEntity;
import utez.edu.mx.melimas.categories.model.CategoryRepository;
import utez.edu.mx.melimas.courses.model.CourseEntity;
import utez.edu.mx.melimas.courses.model.CourseRepository;
import utez.edu.mx.melimas.courses.model.CourseStudentEntity;
import utez.edu.mx.melimas.courses.model.CourseStudentRepository;
import utez.edu.mx.melimas.role.model.RoleEntity;
import utez.edu.mx.melimas.role.model.RoleEnum;
import utez.edu.mx.melimas.role.model.RoleRepository;
import utez.edu.mx.melimas.user.model.UserEntity;
import utez.edu.mx.melimas.user.model.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner{



    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;
    private final CourseStudentRepository courseStudentRepository;
    private static final String IMAGE_BASE_PATH = "uploads/courses/";
    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, CategoryRepository categoryRepository, CourseRepository courseRepository, CourseStudentRepository courseStudentRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryRepository = categoryRepository;
        this.courseRepository = courseRepository;
        this.courseStudentRepository = courseStudentRepository;
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
        UserEntity student = userRepository.findByEmail("student@gmail.com").orElse(null);
        UserEntity teacher = userRepository.findByEmail("teacher@gmail.com").orElse(null);

        // Validar que no sean null
        if (student == null || teacher == null) {
            System.err.println("No se pudieron obtener student o teacher");
            return;
        }
        createCategoryIfNotExists("Programación", "Cursos sobre Java, Python, C++");
        createCategoryIfNotExists("Matemáticas", "Álgebra, cálculo, estadística y más");
        createCategoryIfNotExists("Idiomas", "Inglés, francés, alemán y más");
        createCategoryIfNotExists("Ciencia", "Física, química, biología");
        createCategoryIfNotExists("Negocios", "Administración, finanzas, emprendimiento");
        if (courseRepository.count() == 0) {
            CategoryEntity categoria = categoryRepository.findAll().get(0); // usar la primera categoría

            List<CourseStudentEntity> inscripciones = new ArrayList<>();

            CourseEntity curso1 = new CourseEntity(
                    "Curso Java Básico",
                    "Aprende los fundamentos de Java desde cero",
                    15,
                    IMAGE_BASE_PATH+ "java.png",
                    true,
                    categoria,
                    teacher,
                    inscripciones
            );

            // Crear relación estudiante-curso
            CourseStudentEntity inscripcion1 = new CourseStudentEntity();
            inscripcion1.setCourse(curso1);
            inscripcion1.setStudent(student);
            inscripcion1.setEnrolledAt(java.time.LocalDateTime.now());
            inscripciones.add(inscripcion1);

            courseRepository.save(curso1);
            courseStudentRepository.save(inscripcion1);

            // Repetir para otros cursos
            CourseEntity curso2 = new CourseEntity(
                    "Curso HTML y CSS",
                    "Diseño web con HTML5 y CSS3",
                    10, IMAGE_BASE_PATH+"html_css.png",
                    true,
                    categoria,
                    teacher,
                    new ArrayList<>()
            );
            courseRepository.save(curso2);

            CourseEntity curso3 = new CourseEntity(
                    "Curso de Finanzas para Jóvenes",
                    "Aprende a manejar tu dinero",
                     8,  IMAGE_BASE_PATH+"finanzas.png",
                    true,
                    categoria,
                    teacher,
                    new ArrayList<>()
            );
            courseRepository.save(curso3);
        }
    }
    private void createCategoryIfNotExists(String name, String description) {
        if (!categoryRepository.existsByName(name)) {
            categoryRepository.save(new CategoryEntity(name, description, true));
        }
    }
}
