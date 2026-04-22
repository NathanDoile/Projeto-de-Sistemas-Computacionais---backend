package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.LoginUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaEmailUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaSenhaAtualUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class LoginUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ValidaEmailUsuarioService validaEmailUsuarioService;
    private final ValidaSenhaAtualUsuarioService validaSenhaAtualUsuarioService;

    public void loginUsuario(LoginUsuarioRequest loginUsuarioRequest){

        // Busca o usuário pelo e-mail
        Usuario usuario = usuarioRepository.findByEmail(loginUsuarioRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("E-mail ou senha inválido"));

        if(!usuario.isPossuiVeiculo()){
            usuarioRepository.delete(usuario);
            throw new ResponseStatusException(UNAUTHORIZED, "E-mail ou senha inválido");
        }
        else{
            validaSenhaAtualUsuarioService.validaSenhaAtualUsuario(
                    loginUsuarioRequest.getSenha(),
                    usuario.getIdUsuario()
            );
        }
    }
}