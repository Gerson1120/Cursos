package utez.edu.mx.melimas.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import utez.edu.mx.melimas.security.token.JwtProvider;
import utez.edu.mx.melimas.user.model.UserDTO;
import utez.edu.mx.melimas.user.model.UserEntity;
import utez.edu.mx.melimas.user.model.UserRepository;
import utez.edu.mx.melimas.user.model.UserService;
import utez.edu.mx.melimas.utils.Message;
import utez.edu.mx.melimas.utils.TypesResponse;

import java.util.Optional;

@Service
public class AuthService {

    private final AuthenticationManager manager;

    private final UserRepository repository;

    private final JwtProvider jwtProvider;

    private final UserService userService;

    @Autowired
    public AuthService(AuthenticationManager manager, UserRepository repository, JwtProvider jwtProvider, UserService userService) {
        this.manager = manager;
        this.repository = repository;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    public ResponseEntity<?> login(LoginDto dto) {
        try {
            Authentication auth = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );

            Optional<UserEntity> optionalUser = repository.findByEmail(dto.getEmail());
            System.out.println("usuario = " + optionalUser);

            if (optionalUser.isEmpty() || !optionalUser.get().getStatusActive()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new Message("Usuario no encontrado o inactivo", TypesResponse.WARNING));
            }


            UserEntity user = optionalUser.get();
            String token = jwtProvider.generateToken(auth);
            return ResponseEntity.ok(new SignedDto(token, user));

        } catch (DisabledException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Message("El usuario está deshabilitado", TypesResponse.WARNING));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Message("Correo o contraseña inválidos", TypesResponse.ERROR));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    public ResponseEntity<?> register(UserDTO dto) {
        if (repository.existsByEmail(dto.getEmail()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Correo ya existe");


        return userService.saveUserWithRole(dto, "STUDENT");
    }
}