package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.ExcluirContaUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaSenhaCorretaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExcluirContaUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ValidaSenhaCorretaService validaSenhaCorretaService;

    @Transactional
    public void excluirConta(Long idUsuario, ExcluirContaUsuarioRequest request) {

        validaSenhaCorretaService.porIDESenha(idUsuario, request.getSenha());

        Usuario usuario = usuarioRepository
                .findByIdUsuarioAndIsAtivo(idUsuario, true)
                .orElseThrow();

        usuario.setAtivo(false);

        usuarioRepository.save(usuario);
    }
}