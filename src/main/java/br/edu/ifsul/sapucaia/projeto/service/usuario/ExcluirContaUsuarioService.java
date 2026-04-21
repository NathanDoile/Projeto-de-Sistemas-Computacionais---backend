package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.ExcluirContaUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class ExcluirContaUsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final ValidaUsuarioService validaUsuarioService;

    @Transactional
    public void excluirConta(Long idUsuario, ExcluirContaUsuarioRequest request) {

        validaUsuarioService.porId(idUsuario);
        validarSenhaCorreta(idUsuario, request.getSenha());

        Usuario usuario = usuarioRepository.findByIdUsuarioAndIsAtivo(idUsuario, true)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuário não encontrado."));

        usuario.setAtivo(false);

        usuarioRepository.save(usuario);
    }

    private void validarSenhaCorreta(Long idUsuario, String senha) {
        if (!usuarioRepository.existsByIdUsuarioAndSenhaAndIsAtivoTrue(idUsuario, senha)) {
            throw new ResponseStatusException(UNAUTHORIZED, "Senha incorreta.");
        }
    }
}
