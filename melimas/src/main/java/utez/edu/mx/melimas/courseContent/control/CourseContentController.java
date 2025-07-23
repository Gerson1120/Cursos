package utez.edu.mx.melimas.courseContent.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.melimas.courseContent.model.CourseContentService;
import utez.edu.mx.melimas.user.model.UserService;
import utez.edu.mx.melimas.utils.Message;

@RestController
@RequestMapping("/api/content")
public class CourseContentController {
    private final CourseContentService contentService;
    private final UserService userService;

    @Autowired
    public CourseContentController(CourseContentService contentService, UserService userService) {
        this.contentService = contentService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("saveContent/{courseId}")
    public ResponseEntity<Message> createOrUpdate(
            @PathVariable Long courseId,
            @RequestBody String contentJson
    ) {
        Long teacherId = userService.getCurrentUserId();
        return contentService.createOrUpdateContent(courseId, contentJson, teacherId);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("deleteContent/{courseId}")
    public ResponseEntity<Message> delete(@PathVariable Long courseId) {
        Long teacherId = userService.getCurrentUserId();
        return contentService.deleteContent(courseId, teacherId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("get/{courseId}")
    public ResponseEntity<Message> getContent(@PathVariable Long courseId) {
        Long userId = userService.getCurrentUserId();
        String role = userService.getCurrentUserRole();
        return contentService.getContentByCourseId(courseId, userId, role);
    }
}
