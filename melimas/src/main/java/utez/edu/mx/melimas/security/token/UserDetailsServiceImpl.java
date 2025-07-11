package utez.edu.mx.melimas.security.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.melimas.user.model.UserEntity;
import utez.edu.mx.melimas.user.model.UserRepository;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {


    final private UserRepository usuariosRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        UserEntity usuario = usuariosRepository.findByEmail(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Correo no encontrado: " + correo));

        return UserDetailsImpl.build(usuario);
    }
}
