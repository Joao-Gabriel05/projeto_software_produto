package br.insper.produto.login;
import br.insper.produto.usuario.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.UUID;

@Service
public class LoginService {
    @Autowired
    private RedisTemplate<String, Usuario> tokens;

    public Usuario validateToken(String token) {
        if (tokens.opsForValue().get(token) == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return tokens.opsForValue().get(token);
    }
}
