package utez.edu.mx.melimas.auth;


import com.utez.edu.cursos.Entity.UsuariosEntity;
import com.utez.edu.cursos.Repository.UsuariosRepository;
import com.utez.edu.cursos.security.token.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UsuariosRepository usuarioRepo;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> login(LoginDto dto) {
        try {
            Authentication auth = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getCorreo(), dto.getContraseña())
            );

            UsuariosEntity usuario = usuarioRepo.findByCorreo(dto.getCorreo()).get();
            String token = jwtProvider.generateToken(auth);
            return ResponseEntity.ok(new SignedDto(token, usuario));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    public ResponseEntity<?> register(UsuariosEntity nuevo) {
        if (usuarioRepo.existsByCorreo(nuevo.getCorreo()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Correo ya existe");

        nuevo.setContraseña(passwordEncoder.encode(nuevo.getContraseña()));
        nuevo.setEstado("activo");
        return ResponseEntity.ok(usuarioRepo.save(nuevo));
    }
}