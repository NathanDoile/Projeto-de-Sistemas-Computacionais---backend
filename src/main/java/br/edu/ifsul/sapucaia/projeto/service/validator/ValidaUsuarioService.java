package br.edu.ifsul.sapucaia.projeto.service.validator;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ValidaUsuarioService {

    private final UsuarioRepository usuarioRepository;

    public void porId(Long idUsuario) {

        if(!usuarioRepository.existsByIdUsuarioAndIsAtivo(idUsuario, true)){
            throw new ResponseStatusException(NOT_FOUND, "ID do usuário não existe.");
        }
    }
    public Usuario buscarUsuarioPorId(Long idUsuario) {
        return usuarioRepository
                .findByIdUsuarioAndIsAtivo(idUsuario, true)
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Usuário não encontrado."));
    }
}
