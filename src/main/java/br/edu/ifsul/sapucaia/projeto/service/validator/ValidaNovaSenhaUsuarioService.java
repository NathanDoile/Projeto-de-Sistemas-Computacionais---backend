package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class ValidaNovaSenhaUsuarioService {

    private final PasswordEncoder passwordEncoder;

    public void validaIgualdadeEntreSenhas(Usuario usuario, String novaSenha) {
        if (passwordEncoder.matches(novaSenha, usuario.getSenha())) {
            throw new ResponseStatusException(BAD_REQUEST, "A nova senha precisa ser diferente da atual.");
        }
    }
}