package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.CadastrarUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaEmailUsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.edu.ifsul.sapucaia.projeto.mapper.UsuarioMapper.toEntity;

@Service
@RequiredArgsConstructor
public class CadastrarUsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final ValidaEmailUsuarioService validaEmailUsuarioService;

    @Transactional
    public void cadastrarUsuario(CadastrarUsuarioRequest cadastrarUsuarioRequest){
        validaEmailUsuarioService.validaEmailUnico(cadastrarUsuarioRequest.getEmail());

        Usuario usuario = toEntity(cadastrarUsuarioRequest);

        usuarioRepository.save(usuario);
    }

}
