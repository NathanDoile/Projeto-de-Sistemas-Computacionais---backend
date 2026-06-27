package br.edu.ifsul.sapucaia.projeto.security.service;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.controller.request.RedefinirSenhaRequest;
import br.edu.ifsul.sapucaia.projeto.security.service.validator.ValidaCodigoValidator;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaNovaSenhaUsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class RedefinirSenhaService {

    private final UsuarioRepository usuarioRepository;

    private final ValidaCodigoValidator validaCodigoValidator;

    private final ValidaNovaSenhaUsuarioService validaNovaSenhaUsuarioService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void redefinirSenha(RedefinirSenhaRequest request) {
        Usuario usuario = usuarioRepository.findByEmailAndIsAtivo(request.getEmail(), true)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "E-mail não encontrado."));

        validaCodigoValidator.validarCodigo(usuario, request.getCodigo());
        validaNovaSenhaUsuarioService.validaIgualdadeEntreSenhas(usuario.getIdUsuario(), request.getSenha());

        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setCodigoRedefinirSenha(null);
        usuario.setTentativasRedefinirSenha(-1);
        
        usuarioRepository.save(usuario);
    }
    
}