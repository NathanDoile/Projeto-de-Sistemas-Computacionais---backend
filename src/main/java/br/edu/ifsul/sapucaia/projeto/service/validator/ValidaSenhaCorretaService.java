package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@AllArgsConstructor
public class ValidaSenhaCorretaService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    public void porIDESenha(Long idUsuario, String senha) {
        
        Usuario usuario = usuarioRepository.findByIdUsuarioAndIsAtivo(idUsuario, true)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Senha incorreta."));

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new ResponseStatusException(UNAUTHORIZED, "Senha incorreta.");
        }
    }
}