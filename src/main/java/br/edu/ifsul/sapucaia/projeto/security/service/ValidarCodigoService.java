package br.edu.ifsul.sapucaia.projeto.security.service;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.controller.request.ValidarCodigoRequest;
import br.edu.ifsul.sapucaia.projeto.security.service.validator.ValidaCodigoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ValidarCodigoService {

    private final UsuarioRepository usuarioRepository;
    private final ValidaCodigoValidator validaCodigoValidator;

    public void validar(ValidarCodigoRequest request) {
        Usuario usuario = usuarioRepository.findByEmailAndIsAtivo(request.getEmail(), true)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "E-mail não encontrado."));

        validaCodigoValidator.validarCodigo(usuario, request.getCodigo());
    }
    
}