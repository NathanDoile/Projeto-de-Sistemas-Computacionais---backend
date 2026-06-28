package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class ValidaSenhaAtualUsuarioService {

    private final UsuarioRepository usuarioRepository;
    
    private final PasswordEncoder passwordEncoder;

    public void validaSenhaAtualUsuario(String senhaAtual, Long id){
        
        Usuario usuario = usuarioRepository.findByIdUsuarioAndIsAtivo(id, true)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "E-mail ou senha inválido"));

        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new ResponseStatusException(BAD_REQUEST, "E-mail ou senha inválido");
        }
    }
}