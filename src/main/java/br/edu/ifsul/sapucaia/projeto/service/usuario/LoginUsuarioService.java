package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.LoginUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.controller.response.LoginUsuarioResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.mapper.UsuarioMapper;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaSenhaAtualUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class LoginUsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final ValidaSenhaAtualUsuarioService validaSenhaAtualUsuarioService;

    private final ValidaUsuarioService validaUsuarioService;

    public LoginUsuarioResponse loginUsuario(LoginUsuarioRequest loginUsuarioRequest) {

        validaUsuarioService.porEmail(loginUsuarioRequest.getEmail());

        Usuario usuario = usuarioRepository
                .findByEmailAndIsAtivo(loginUsuarioRequest.getEmail(), true)
                .get();

        if (!usuario.isPossuiVeiculo()) {
            usuarioRepository.delete(usuario);
            throw new ResponseStatusException(
                    UNAUTHORIZED,
                    "E-mail ou senha inválido"
            );
        }

        validaSenhaAtualUsuarioService.validaSenhaAtualUsuario(
                loginUsuarioRequest.getSenha(),
                usuario.getIdUsuario()
        );

        return UsuarioMapper.toResponseLogin(usuario);
    }
}