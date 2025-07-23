package utez.edu.mx.melimas.passwordReset;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cursos/auth/password")
@CrossOrigin("*")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/request-reset")
    public ResponseEntity<?> requestReset(@RequestParam("email") String email) {
        try {
            passwordResetService.requestReset(email);
            return ResponseEntity.ok("Si el correo existe, se ha enviado un enlace de recuperación.");
        } catch (MessagingException e) {
            return ResponseEntity.internalServerError().body("No se pudo enviar el correo.");
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto dto) {
        boolean success = passwordResetService.resetPassword(dto.getToken(), dto.getNewPassword());
        return success ?
                ResponseEntity.ok("Contraseña restablecida correctamente") :
                ResponseEntity.badRequest().body("Token inválido o expirado");
    }
}