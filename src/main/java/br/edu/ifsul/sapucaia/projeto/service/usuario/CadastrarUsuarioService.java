package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.CadastrarUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.usuario.CadastrarUsuarioResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.mapper.UsuarioMapper;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaEmailUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaTelefoneUsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.edu.ifsul.sapucaia.projeto.mapper.UsuarioMapper.toEntity;
import static br.edu.ifsul.sapucaia.projeto.mapper.UsuarioMapper.toResponse;

@Service
@RequiredArgsConstructor
public class CadastrarUsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final ValidaEmailUsuarioService validaEmailUsuarioService;

    private final ValidaTelefoneUsuarioService validaTelefoneUsuarioService;

    @Transactional
    public CadastrarUsuarioResponse cadastrarUsuario(CadastrarUsuarioRequest cadastrarUsuarioRequest){
        validaEmailUsuarioService.validaEmailUnico(cadastrarUsuarioRequest.getEmail());
        validaTelefoneUsuarioService.validaTelefoneUnico(cadastrarUsuarioRequest.getTelefone());

        Usuario usuario = toEntity(cadastrarUsuarioRequest);

        usuario.setAtivo(true);
        usuario.setPossuiVeiculo(false);

        usuarioRepository.save(usuario);

        return toResponse(usuario);
    }

}
