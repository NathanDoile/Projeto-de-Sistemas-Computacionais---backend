package br.edu.ifsul.sapucaia.projeto.service.usuario;

import org.springframework.stereotype.Service;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.EditarPerfilUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaEmailUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaTelefoneUsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EditarPerfilUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ValidaEmailUsuarioService validaEmailUsuarioService;
    private final ValidaTelefoneUsuarioService validaTelefoneUsuarioService;

    @Transactional
    public void editarPerfilUsuario(Long idUsuario, EditarPerfilUsuarioRequest request) {

        if (request.getEmail() != null) {
            validaEmailUsuarioService.validaEmailUnicoParaEdicao(request.getEmail(), idUsuario);
        }

        if (request.getTelefone() != null) {
            validaTelefoneUsuarioService.validaTelefoneUnicoParaEdicao(request.getTelefone(), idUsuario);
        }

        Usuario usuario = usuarioRepository
                .findByIdUsuarioAndIsAtivo(idUsuario, true)
                .orElseThrow();

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