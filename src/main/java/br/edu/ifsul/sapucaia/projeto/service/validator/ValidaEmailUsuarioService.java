package br.edu.ifsul.sapucaia.projeto.service.validator;


import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class ValidaEmailUsuarioService {

    private final UsuarioRepository usuarioRepository;

    public void validaEmailUnico(String email){
        if(usuarioRepository.existsByEmail(email))
            throw new ResponseStatusException(BAD_REQUEST, "Esse email já foi cadastrado.");
    }

    public void validaEmailUnicoParaEdicao(String email, Long idUsuario){
        if(usuarioRepository.existsByEmailAndIdUsuarioNot(email, idUsuario))
            throw new ResponseStatusException(BAD_REQUEST, "Esse email já foi cadastrado.");
    }
}
