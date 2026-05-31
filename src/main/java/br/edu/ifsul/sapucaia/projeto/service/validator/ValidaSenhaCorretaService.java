package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@AllArgsConstructor
public class ValidaSenhaCorretaService {

    private final UsuarioRepository usuarioRepository;

    public void porIDESenha(Long idUsuario, String senha) {
        if (!usuarioRepository.existsByIdUsuarioAndSenhaAndIsAtivoTrue(idUsuario, senha)) {
            throw new ResponseStatusException(UNAUTHORIZED, "Senha incorreta.");
        }
    }
}
