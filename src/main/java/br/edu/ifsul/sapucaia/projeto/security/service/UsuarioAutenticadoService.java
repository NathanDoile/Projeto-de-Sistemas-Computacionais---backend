package br.edu.ifsul.sapucaia.projeto.security.service;

import br.edu.ifsul.sapucaia.projeto.controller.response.LoginUsuarioResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static br.edu.ifsul.sapucaia.projeto.mapper.UsuarioMapper.toResponseLogin;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class UsuarioAutenticadoService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioSecurity getUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (UsuarioSecurity) authentication.getPrincipal();
    }

    public LoginUsuarioResponse getResponse() {

        UsuarioSecurity user = getUser();

        if(isNull(user)){
            return null;
        }

        Usuario usuario = usuarioRepository.findByIdUsuarioAndIsAtivo(user.getId(), true).get();

        return toResponseLogin(usuario);

    }
}
