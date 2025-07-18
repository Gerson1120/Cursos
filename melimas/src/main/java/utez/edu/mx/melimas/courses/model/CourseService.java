package utez.edu.mx.melimas.courses.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import utez.edu.mx.melimas.categories.model.CategoryEntity;
import utez.edu.mx.melimas.categories.model.CategoryRepository;
import utez.edu.mx.melimas.courses.model.studentCourse.EnrolledStudentDTO;
import utez.edu.mx.melimas.user.model.UserEntity;
import utez.edu.mx.melimas.user.model.UserRepository;
import utez.edu.mx.melimas.utils.Message;
import utez.edu.mx.melimas.utils.TypesResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CourseStudentRepository enrollmentRepository;

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    public CourseService(CourseRepository courseRepository, UserRepository userRepository, CategoryRepository categoryRepository, CourseStudentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<Message> save(CourseDTO dto, MultipartFile file) throws IOException {
        try {
            Optional<UserEntity> optionalTeacher = userRepository.findById(dto.getTeacherId());
            if (optionalTeacher.isEmpty() || !optionalTeacher.get().getRol().getRoleEnum().name().equals("TEACHER")) {
                return new ResponseEntity<>(new Message("Invalid or missing teacher", null, TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }

            CourseEntity course = new CourseEntity();
            course.setName(dto.getName());
            course.setDescription(dto.getDescription());
            course.setDuration(dto.getDuration());
            course.setImageUrl(dto.getImageUrl());
            course.setTeacher(optionalTeacher.get());
            course.setEnabled(true);

            if (dto.getCategoryId() != null) {
                Optional<CategoryEntity> optionalCategory = categoryRepository.findById(dto.getCategoryId());
                optionalCategory.ifPresent(course::setCategory);
            }

            if (file != null && !file.isEmpty()) {
                String imageUrl = saveImage(file);
                course.setImageUrl(imageUrl);
            }

            course = courseRepository.saveAndFlush(course);
            log.info("Course '{}' created with ID {}", course.getName(), course.getId());
            return new ResponseEntity<>(new Message("Course created", course, TypesResponse.SUCCESS), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error saving course: {}", e.getMessage());
            return new ResponseEntity<>(new Message("Error saving course", null, TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Message> findAll() {
        List<CourseSimpleDTO> list = courseRepository.findAllSummary();
        log.info("Cursos en findAll {}" , list);
        return new ResponseEntity<>(new Message("Course list", list, TypesResponse.SUCCESS), HttpStatus.OK);
    }

    public ResponseEntity<Message> findOne(Long id) {
        Optional<CourseEntity> optional = courseRepository.findById(id);
        if (optional.isEmpty())
            return new ResponseEntity<>(new Message("Course not found", TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new Message("Course found", optional.get(), TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<Message> update(Long id, CourseDTO dto, MultipartFile file) throws IOException {
        Optional<CourseEntity> optional = courseRepository.findById(id);
        if (optional.isEmpty())
            return new ResponseEntity<>(new Message("Course not found", TypesResponse.WARNING), HttpStatus.NOT_FOUND);

        CourseEntity course = optional.get();
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setDuration(dto.getDuration());

        if (dto.getCategoryId() != null) {
            Optional<CategoryEntity> category = categoryRepository.findById(dto.getCategoryId());
            category.ifPresent(course::setCategory);
        }

        if (file != null && !file.isEmpty()) {
            String previousImagePath = course.getImageUrl();
            if (previousImagePath != null) {
                try {
                    Path path = Paths.get("." + previousImagePath);
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    log.warn("No se pudo eliminar la imagen anterior: {}", previousImagePath);
                }
            }

            String imageUrl = saveImage(file);
            course.setImageUrl(imageUrl);
        }

        courseRepository.saveAndFlush(course);
        return new ResponseEntity<>(new Message("Course updated", course, TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<Message> disable(Long id) {
        Optional<CourseEntity> optional = courseRepository.findById(id);
        if (optional.isEmpty())
            return new ResponseEntity<>(new Message("Course not found", TypesResponse.WARNING), HttpStatus.NOT_FOUND);

        CourseEntity course = optional.get();
        course.setEnabled(!course.isEnabled());
        courseRepository.saveAndFlush(course);
        return new ResponseEntity<>(new Message("Course disabled", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<Message> enrollStudent(Long courseId, Long studentId) {
        Optional<CourseEntity> courseOpt = courseRepository.findById(courseId);
        Optional<UserEntity> studentOpt = userRepository.findById(studentId);

        if (courseOpt.isEmpty() || studentOpt.isEmpty()) {
            return new ResponseEntity<>(new Message("Course or student not found", TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }

        CourseEntity course = courseOpt.get();
        if (!course.isEnabled()) {
            return new ResponseEntity<>(new Message("Course is disabled", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        UserEntity student = studentOpt.get();
        CourseStudentEntity enrollment = new CourseStudentEntity();
        enrollment.setCourse(course);
        enrollment.setStudent(student);
        enrollmentRepository.save(enrollment);
        return new ResponseEntity<>(new Message("Student enrolled", TypesResponse.SUCCESS), HttpStatus.OK);
    }



    public String saveImage(MultipartFile file) throws IOException {
        String uploadDir = "uploads/courses/";
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/courses/" + fileName;
    }

    public ResponseEntity<Message> enrollStudentByEmail(Long courseId, String email) {
        Optional<CourseEntity> courseOpt = courseRepository.findById(courseId);
        Optional<UserEntity> studentOpt = userRepository.findByEmail(email);

        if (courseOpt.isEmpty() || studentOpt.isEmpty()) {
            return new ResponseEntity<>(new Message("Course or student not found", TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }

        CourseEntity course = courseOpt.get();
        if (!course.isEnabled()) {
            return new ResponseEntity<>(new Message("Course is disabled", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        UserEntity student = studentOpt.get();
        if (enrollmentRepository.findByCourseIdAndStudent_Id(courseId, student.getId()).isPresent()) {
            return new ResponseEntity<>(new Message("Already enrolled", TypesResponse.WARNING), HttpStatus.CONFLICT);
        }

        CourseStudentEntity enrollment = new CourseStudentEntity();
        enrollment.setCourse(course);
        enrollment.setStudent(student);
        enrollmentRepository.save(enrollment);
        return new ResponseEntity<>(new Message("Student enrolled", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    public ResponseEntity<Message> unenrollStudentByEmail(Long courseId, String email) {
        Optional<UserEntity> studentOpt = userRepository.findByEmail(email);
        if (studentOpt.isEmpty()) {
            return new ResponseEntity<>(new Message("Student not found", TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }

        Optional<CourseStudentEntity> enrollmentOpt =
                enrollmentRepository.findByCourseIdAndStudentId(courseId, studentOpt.get().getId());

        if (enrollmentOpt.isEmpty()) {
            return new ResponseEntity<>(new Message("Enrollment not found", TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }

        enrollmentRepository.delete(enrollmentOpt.get());
        return new ResponseEntity<>(new Message("Student unenrolled", TypesResponse.SUCCESS), HttpStatus.OK);
    }


    public ResponseEntity<Message> getStudentsByCourseAndTeacherEmail(Long courseId, String email) {
        Optional<CourseEntity> courseOpt = courseRepository.findById(courseId);
        Optional<UserEntity> teacherOpt = userRepository.findByEmail(email);

        if (courseOpt.isEmpty() || teacherOpt.isEmpty()) {
            return new ResponseEntity<>(new Message("Course or teacher not found", TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }

        CourseEntity course = courseOpt.get();
        if (!course.getTeacher().getId().equals(teacherOpt.get().getId())) {
            return new ResponseEntity<>(new Message("You are not the owner of this course", TypesResponse.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }

        List<EnrolledStudentDTO> students = course.getEnrollments()
                .stream()
                .map(enrollment -> {
                    UserEntity s = enrollment.getStudent();
                    return new EnrolledStudentDTO(
                            s.getId(),
                            s.getName(),
                            s.getLastName(),
                            s.getEmail()
                    );
                })
                .toList();

        return new ResponseEntity<>(new Message("Enrolled students", students, TypesResponse.SUCCESS), HttpStatus.OK);
    }

    public ResponseEntity<Message> getCoursesByStudentEmail(String email) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(new Message("Estudiante no encontrado", null, TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }

        List<CourseSimpleDTO> courses  = enrollmentRepository.findCoursesByStudentEmail(email);

        // Puedes devolver los cursos como DTO si quieres evitar enviar objetos grandes
        return new ResponseEntity<>(new Message("Cursos inscritos", courses, TypesResponse.SUCCESS), HttpStatus.OK);
    }

}
