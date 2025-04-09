package br.insper.produto.usuario;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario saveUsuario(Usuario usuario) {
        if (usuario.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (usuario.getNome() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        String hash = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
        usuario.setSenha(hash);

        return usuarioRepository.save(usuario);
    }

    public Usuario findUsuarioByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return usuario;
    }

    public void deleteUsuario(String email) {
        Usuario usuario = findUsuarioByEmail(email);
        usuarioRepository.delete(usuario);
    }

    public CountUsuarioDTO countUsuarios() {
        Long count = usuarioRepository.count();
        return new CountUsuarioDTO(count);
    }

    public Usuario validateUser(String user, String password) {
        Usuario usuario = findUsuarioByEmail(user);

        if (!BCrypt.checkpw(password, usuario.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return usuario;

    }


}