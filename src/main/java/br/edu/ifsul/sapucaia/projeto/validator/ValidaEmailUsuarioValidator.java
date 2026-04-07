package br.edu.ifsul.sapucaia.projeto.validator;


import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class ValidaEmailUsuarioValidator {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public void validaEmailUnico(String email){
        if(usuarioRepository.existsByEmail(email))
            throw new ResponseStatusException(BAD_REQUEST, "Esse email já cadastrado.");
    }
}
