package utez.edu.mx.melimas.courseContent.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utez.edu.mx.melimas.courses.model.CourseEntity;
import utez.edu.mx.melimas.courses.model.CourseRepository;
import utez.edu.mx.melimas.courses.model.CourseStudentRepository;
import utez.edu.mx.melimas.utils.Message;
import utez.edu.mx.melimas.utils.TypesResponse;

import java.util.Map;
import java.util.Optional;

@Service
public class CourseContentService {

    private static final Logger log = LoggerFactory.getLogger(CourseContentService.class);
    private final CourseContentRepository contentRepository;
    private final CourseRepository courseRepository;
    private final CourseStudentRepository courseStudentRepository;


    @Autowired
    public CourseContentService(CourseContentRepository contentRepository, CourseRepository courseRepository, CourseStudentRepository courseStudentRepository) {
        this.contentRepository = contentRepository;
        this.courseRepository = courseRepository;
        this.courseStudentRepository = courseStudentRepository;
    }

    public ResponseEntity<Message> getContentByCourseId(Long courseId, Long userId, String roleName) {
        Optional<CourseContentEntity> optionalContent = contentRepository.findByCourseId(courseId);
        if (optionalContent.isEmpty()) {
            return new ResponseEntity<>(new Message("Contenido no encontrado", null, TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }

        CourseContentEntity content = optionalContent.get();
        CourseEntity course = content.getCourse();

        // ADMIN accede libremente
        if ("ADMIN".equals(roleName)) {
            return buildOkResponse(content);
        }

        // TEACHER solo si es dueño del curso
        if ("TEACHER".equals(roleName) && course.getTeacher().getId().equals(userId)) {
            return buildOkResponse(content);
        }

        // STUDENT solo si está inscrito al curso
        if ("STUDENT".equals(roleName)) {
            boolean isEnrolled = courseStudentRepository
                    .findByCourseIdAndStudentId(courseId, userId)
                    .isPresent();
            if (isEnrolled) {
                return buildOkResponse(content);
            }
        }

        return new ResponseEntity<>(new Message("No tienes permiso para ver este contenido", null, TypesResponse.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<Message> buildOkResponse(CourseContentEntity content) {
        CourseContentDTO dto = new CourseContentDTO(content.getId(), content.getCourse().getId(), content.getContentJson());
        return new ResponseEntity<>(new Message("Contenido obtenido", dto, TypesResponse.SUCCESS), HttpStatus.OK);
    }

    public ResponseEntity<Message> createOrUpdateContent(Long courseId, String contentJson, Long teacherId) {
        CourseEntity course = courseRepository.findById(courseId).orElse(null);
        if (course == null || !course.getTeacher().getId().equals(teacherId)) {
            return new ResponseEntity<>(new Message("Acceso denegado", null, TypesResponse.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> parsedJson = mapper.readValue(contentJson, Map.class);

            CourseContentEntity content = contentRepository.findByCourseId(courseId).orElse(new CourseContentEntity());
            content.setCourse(course);
            content.setContentJson(parsedJson);

            CourseContentEntity saved = contentRepository.save(content);
            CourseContentDTO dto = new CourseContentDTO(saved.getId(), courseId, saved.getContentJson());

            return new ResponseEntity<>(
                    new Message("Contenido actualizado", dto, TypesResponse.SUCCESS),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Error al procesar el JSON", null, TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Message> deleteContent(Long courseId, Long teacherId) {
        CourseEntity course = courseRepository.findById(courseId).orElse(null);
        if (course == null || !course.getTeacher().getId().equals(teacherId)) {
            return new ResponseEntity<>(new Message("Acceso denegado", null, TypesResponse.UNAUTHORIZED),
                    HttpStatus.UNAUTHORIZED);
        }

        Optional<CourseContentEntity> optionalContent = contentRepository.findByCourseId(courseId);
        if (optionalContent.isEmpty()) {
            return new ResponseEntity<>(new Message("Contenido no encontrado", null, TypesResponse.WARNING),
                    HttpStatus.NOT_FOUND);
        }
        course.setContent(null);
        courseRepository.save(course);
        log.info("Borrando contenido de curso {}",optionalContent.get().getId());
        contentRepository.deleteById(optionalContent.get().getId());

        return new ResponseEntity<>(new Message("Contenido borrado", null, TypesResponse.SUCCESS), HttpStatus.OK);
    }
}
