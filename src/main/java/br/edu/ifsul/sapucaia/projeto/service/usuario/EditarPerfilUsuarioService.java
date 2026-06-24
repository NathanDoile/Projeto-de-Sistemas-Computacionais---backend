package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.EditarPerfilUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaEmailUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaTelefoneUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EditarPerfilUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ValidaEmailUsuarioService validaEmailUsuarioService;
    private final ValidaTelefoneUsuarioService validaTelefoneUsuarioService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    @Transactional
    public void editarPerfilUsuario(EditarPerfilUsuarioRequest request) {

        UsuarioSecurity usuarioLogado = usuarioAutenticadoService.getUser();

        Usuario usuario = usuarioRepository.findById(usuarioLogado.getId()).get();

        if (request.getEmail() != null) {
            validaEmailUsuarioService.validaEmailUnicoParaEdicao(
                    request.getEmail(),
                    usuario.getIdUsuario()
            );
        }

        if (request.getTelefone() != null) {
            validaTelefoneUsuarioService.validaTelefoneUnicoParaEdicao(
                    request.getTelefone(),
                    usuario.getIdUsuario()
            );
        }

        atualizaUsuario(usuario, request);

        usuarioRepository.save(usuario);
    }

    private void atualizaUsuario(Usuario usuario, EditarPerfilUsuarioRequest request) {

        if (request.getNome() != null) {
            usuario.setNome(request.getNome());
        }

        if (request.getEmail() != null) {
            usuario.setEmail(request.getEmail());
        }

        if (request.getTelefone() != null) {
            usuario.setTelefone(request.getTelefone());
        }
    }
}