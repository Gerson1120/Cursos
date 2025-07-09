package utez.edu.mx.melimas.auth;


import com.utez.edu.cursos.Entity.UsuariosEntity;

public class SignedDto {
    private String token;
    private String tokenType = "Bearer";
    private UsuariosEntity user;

    public SignedDto(String token, UsuariosEntity user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public UsuariosEntity getUser() {
        return user;
    }

    public void setUser(UsuariosEntity user) {
        this.user = user;
    }
}
