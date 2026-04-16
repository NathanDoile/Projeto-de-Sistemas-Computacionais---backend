package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class ValidaSenhaAtualUsuarioService {

    private final UsuarioRepository usuarioRepository;

    public void validaSenhaAtualUsuario(String senhaAtual, Long id){

        if(!usuarioRepository.existsByIdUsuarioAndSenha(id, senhaAtual)){
            throw new ResponseStatusException(BAD_REQUEST, "Senha atual incorreta.");
        }
    }

}
