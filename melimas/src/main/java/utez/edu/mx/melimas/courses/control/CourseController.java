package utez.edu.mx.melimas.courses.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import utez.edu.mx.melimas.courses.model.CourseDTO;
import utez.edu.mx.melimas.courses.model.CourseService;
import utez.edu.mx.melimas.utils.Message;
import utez.edu.mx.melimas.utils.TypesResponse;

import java.io.IOException;

@Service
public class CourseController {
    private final CourseService courseService;
    private MultipartFile  file;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody CourseDTO dto) {
        return courseService.save(dto);
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

}
