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

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    public CourseService(CourseRepository courseRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
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
        course.setEnabled(false);
        courseRepository.saveAndFlush(course);
        return new ResponseEntity<>(new Message("Course disabled", TypesResponse.SUCCESS), HttpStatus.OK);
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

}
