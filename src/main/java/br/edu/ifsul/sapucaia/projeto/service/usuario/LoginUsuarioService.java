package br.edu.ifsul.sapucaia.projeto.service.usuario;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.LoginUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaEmailUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaSenhaAtualUsuarioService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ValidaEmailUsuarioService validaEmailUsuarioService;
    private final ValidaSenhaAtualUsuarioService validaSenhaAtualUsuarioService;

    @Transactional(readOnly = true)
    public void loginUsuario(LoginUsuarioRequest loginUsuarioRequest){

        // Valida o e-mail informado
        //validaEmailUsuarioService.validarEmailUsuario(loginUsuarioRequest.getEmail());

        // Busca o usuário pelo e-mail
        Usuario usuario = usuarioRepository.findByEmail(loginUsuarioRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para o e-mail informado."));

        // Valida se a senha está correta
        validaSenhaAtualUsuarioService.validaSenhaAtualUsuario(
                loginUsuarioRequest.getSenha(),
                usuario.getSenha()
        );
    }
}