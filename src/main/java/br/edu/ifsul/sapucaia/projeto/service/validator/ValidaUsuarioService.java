package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ValidaUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void porId(Long idUsuario) {

        if(!usuarioRepository.existsById(idUsuario)){
            throw new ResponseStatusException(NOT_FOUND, "ID do usuário não existe.");
        }
    }
}
