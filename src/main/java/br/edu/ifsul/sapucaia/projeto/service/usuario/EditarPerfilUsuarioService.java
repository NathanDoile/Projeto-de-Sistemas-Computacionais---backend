package br.edu.ifsul.sapucaia.projeto.service.usuario;

import org.springframework.stereotype.Service;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.EditarPerfilUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.mapper.UsuarioMapper;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaEmailUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaTelefoneUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EditarPerfilUsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final ValidaUsuarioService validaUsuarioService;

    private final ValidaEmailUsuarioService validaEmailUsuarioService;

    private final ValidaTelefoneUsuarioService validaTelefoneUsuarioService;

    @Transactional
    public void editarPerfilUsuario(Long idUsuario, EditarPerfilUsuarioRequest editarPerfilUsuarioRequest) {
        validaUsuarioService.porId(idUsuario);

        if (editarPerfilUsuarioRequest.getEmail() != null) {
            validaEmailUsuarioService.validaEmailUnicoParaEdicao(editarPerfilUsuarioRequest.getEmail(), idUsuario);
        }

        if (editarPerfilUsuarioRequest.getTelefone() != null) {
            validaTelefoneUsuarioService.validaTelefoneUnicoParaEdicao(editarPerfilUsuarioRequest.getTelefone(), idUsuario);
        }

        Usuario usuario = usuarioRepository.findById(idUsuario).get();

        UsuarioMapper.updateEntity(usuario, editarPerfilUsuarioRequest);

        usuarioRepository.save(usuario);

    }
    
}
