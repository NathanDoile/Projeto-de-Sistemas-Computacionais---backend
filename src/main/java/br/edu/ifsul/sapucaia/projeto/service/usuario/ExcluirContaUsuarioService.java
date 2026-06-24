package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.ExcluirContaUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaSenhaCorretaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ExcluirContaUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ValidaSenhaCorretaService validaSenhaCorretaService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    @Transactional
    public void excluirConta(ExcluirContaUsuarioRequest request) {

        UsuarioSecurity usuarioLogado = usuarioAutenticadoService.getUser();

        validaSenhaCorretaService.porIDESenha(
                usuarioLogado.getId(),
                request.getSenha()
        );

        Usuario usuario = usuarioRepository.findById(usuarioLogado.getId()).get();

        usuario.setAtivo(false);

        usuarioRepository.save(usuario);
    }
}