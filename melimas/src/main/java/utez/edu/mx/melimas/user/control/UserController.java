package utez.edu.mx.melimas.user.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.melimas.user.model.UserDTO;
import utez.edu.mx.melimas.user.model.UserService;
import utez.edu.mx.melimas.utils.Message;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register/student")
    public ResponseEntity<Message> registerStudent(@RequestBody UserDTO dto) {
        return userService.saveUserWithRole(dto, "STUDENT");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/teacher")
    public ResponseEntity<Message> registerTeacher(@RequestBody UserDTO dto) {
        return userService.saveUserWithRole(dto, "TEACHER");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public ResponseEntity<Message> getAllUsers() {
        return userService.findAll();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<Message> updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
        return userService.updateUser(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Message> deleteUser(@PathVariable Long id) {
        return userService.logicalDeleteUser(id);
    }
}
