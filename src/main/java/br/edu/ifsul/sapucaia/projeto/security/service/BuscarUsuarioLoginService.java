package br.edu.ifsul.sapucaia.projeto.security.service;

import br.edu.ifsul.sapucaia.projeto.controller.response.LoginUsuarioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarUsuarioLoginService {

    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public LoginUsuarioResponse buscar() {

        return usuarioAutenticadoService.getResponse();
    }
}
