package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.CadastrarUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.validator.ValidaEmailUsuarioValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.edu.ifsul.sapucaia.projeto.mapper.UsuarioMapper.toEntity;

@Service
public class CadastrarUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ValidaEmailUsuarioValidator validaEmailUsuarioValidator;

    @Transactional
    public void cadastrarUsuario(CadastrarUsuarioRequest cadastrarUsuarioRequest){
        validaEmailUsuarioValidator.validaEmailUnico(cadastrarUsuarioRequest.getEmail());

        Usuario usuario = toEntity(cadastrarUsuarioRequest);

        usuarioRepository.save(usuario);
    }

}
