package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.AlterarSenhaUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaNovaSenhaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaSenhaAtualUsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class AlterarSenhaUsuarioService {
    private final UsuarioRepository usuarioRepository;

    private final UsuarioAutenticadoService usuarioAutenticadoService;

    private final ValidaSenhaAtualUsuarioService validaSenhaAtualUsuarioService;

    private final ValidaNovaSenhaUsuarioService validaNovaSenhaUsuarioService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void alterarSenhaUsuario(AlterarSenhaUsuarioRequest alterarSenhaUsuarioRequest){

        UsuarioSecurity usuarioLogado = usuarioAutenticadoService.getUser();

        validaSenhaAtualUsuarioService.validaSenhaAtualUsuario(alterarSenhaUsuarioRequest.getSenhaAtual(), usuarioLogado.getId());

        Usuario usuario = usuarioRepository.findByIdUsuarioAndIsAtivo(usuarioLogado.getId(), true).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuário não encontrado."));

        validaNovaSenhaUsuarioService.validaIgualdadeEntreSenhas(usuario, alterarSenhaUsuarioRequest.getNovaSenha());

        usuario.setSenha(passwordEncoder.encode(alterarSenhaUsuarioRequest.getNovaSenha()));

        usuarioRepository.save(usuario);
        
    }
}