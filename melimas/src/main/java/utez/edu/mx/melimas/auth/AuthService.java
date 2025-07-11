package utez.edu.mx.melimas.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import utez.edu.mx.melimas.security.token.JwtProvider;
import utez.edu.mx.melimas.user.model.UserEntity;
import utez.edu.mx.melimas.user.model.UserRepository;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserRepository reopository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> login(LoginDto dto) {
        try {
            Authentication auth = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );

            UserEntity usuario = reopository.findByEmail(dto.getEmail()).get();
            System.out.println("usuario = " + usuario);
            String token = jwtProvider.generateToken(auth);
            return ResponseEntity.ok(new SignedDto(token, usuario));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    public ResponseEntity<?> register(UserEntity nuevo) {
        if (reopository.existsByEmail(nuevo.getEmail()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Correo ya existe");

        nuevo.setPassword(passwordEncoder.encode(nuevo.getPassword()));
        nuevo.setStatusActive(true);
        return ResponseEntity.ok(reopository.save(nuevo));
    }
}