package utez.edu.mx.melimas.security.token;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import utez.edu.mx.melimas.user.model.UserEntity;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private final UserEntity userEntity;

    public UserDetailsImpl(UserEntity usuario) {
        this.userEntity = usuario;
    }

    public static UserDetailsImpl build(UserEntity usuario) {
        return new UserDetailsImpl(usuario);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userEntity.getRol()));
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return userEntity.getStatusActive();
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }
}
