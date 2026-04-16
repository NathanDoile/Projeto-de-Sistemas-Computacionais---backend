package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class ValidaNovaSenhaUsuarioService {

    private final UsuarioRepository usuarioRepository;

    public void validaIgualdadeEntreSenhas(Long id, String novaSenha){
        if(usuarioRepository.existsByIdUsuarioAndSenha(id, novaSenha)){
            throw new ResponseStatusException(BAD_REQUEST, "A nova senha precisa ser diferente da atual.");
        }
    }
}
