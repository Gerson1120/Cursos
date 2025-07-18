package utez.edu.mx.melimas.courses.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utez.edu.mx.melimas.courses.model.CourseDTO;
import utez.edu.mx.melimas.courses.model.CourseService;
import utez.edu.mx.melimas.courses.model.EnrollmentRequestDTO;
import utez.edu.mx.melimas.courses.model.studentCourse.CourseViewRequestDTO;
import utez.edu.mx.melimas.utils.Message;
import utez.edu.mx.melimas.utils.TypesResponse;

import java.io.IOException;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {
    private final CourseService courseService;
    private MultipartFile file;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/save-course")
    public ResponseEntity<Message> create(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("duration") int duration,
            @RequestParam("teacherId") Long teacherId,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws IOException {
        CourseDTO dto = new CourseDTO();
        dto.setName(name);
        dto.setDescription(description);
        dto.setDuration(duration);
        dto.setTeacherId(teacherId);
        dto.setCategoryId(categoryId);
        return courseService.save(dto, file);
    }

    @GetMapping("/findAll")
    public ResponseEntity<Message> getAll() {
        return courseService.findAll();
    }

    @GetMapping("/findOne/{id}")
    public ResponseEntity<Message> getOne(@PathVariable Long id) {
        return courseService.findOne(id);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/update-course/{id}")
    public ResponseEntity<Message> update(
            @PathVariable Long id, @RequestParam("name") String name, @RequestParam("description") String description,
            @RequestParam("duration") int duration, @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "syllabus", required = false) String syllabus,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws IOException {
        CourseDTO dto = new CourseDTO();
        dto.setName(name);
        dto.setDescription(description);
        dto.setDuration(duration);
        dto.setSyllabus(syllabus);
        dto.setCategoryId(categoryId);
        return courseService.update(id, dto, file);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/disable/{id}")
    public ResponseEntity<Message> delete(@PathVariable Long id) {
        return courseService.disable(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @PostMapping("/upload-image")
    public ResponseEntity<Message> uploadImage(@RequestParam("file") MultipartFile file) {
        this.file = file;
        try {
            String imageUrl = courseService.saveImage(file);
            return ResponseEntity.ok(new Message("Image uploaded", imageUrl, TypesResponse.SUCCESS));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new Message("Failed to upload image", null, TypesResponse.ERROR));
        }
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/enroll")
    public ResponseEntity<Message> enroll(@RequestBody EnrollmentRequestDTO request, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return courseService.enrollStudentByEmail(request.getCourseId(), email);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/unenroll")
    public ResponseEntity<Message> unenroll(@RequestBody EnrollmentRequestDTO request, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return courseService.unenrollStudentByEmail(request.getCourseId(), email);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/view-students")
    public ResponseEntity<Message> viewStudents(@RequestBody CourseViewRequestDTO request, @AuthenticationPrincipal UserDetails userDetails) {
        return courseService.getStudentsByCourseAndTeacherEmail(request.getCourseId(), userDetails.getUsername());
    }


    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/my-courses")
    public ResponseEntity<Message> getMyCourses() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return courseService.getCoursesByStudentEmail(email);
    }
}
