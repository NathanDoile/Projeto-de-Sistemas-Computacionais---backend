package br.edu.ifsul.sapucaia.projeto.service.validator;

import static org.springframework.http.HttpStatus.CONFLICT;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidaTelefoneUsuarioService {

    private final UsuarioRepository usuarioRepository;

    public void validaTelefoneUnico(String telefone){
        if(usuarioRepository.existsByTelefone(telefone, true))
            throw new ResponseStatusException(CONFLICT, "Esse telefone já foi cadastrado.");
    }

    public void validaTelefoneUnicoParaEdicao(String telefone, Long idUsuario){
        if(usuarioRepository.existsByTelefoneAndIdUsuarioNot(telefone, idUsuario, true))
            throw new ResponseStatusException(CONFLICT, "Esse telefone já foi cadastrado.");
    }
    
}
