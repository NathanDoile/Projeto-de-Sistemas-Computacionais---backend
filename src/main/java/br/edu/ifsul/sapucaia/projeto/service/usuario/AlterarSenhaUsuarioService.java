package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.AlterarSenhaUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaNovaSenhaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaSenhaAtualUsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AlterarSenhaUsuarioService {
    private final UsuarioRepository usuarioRepository;

    private final ValidaSenhaAtualUsuarioService validaSenhaAtualUsuarioService;

    private final ValidaNovaSenhaUsuarioService validaNovaSenhaUsuarioService;

    @Transactional
    public void     alterarSenhaUsuario(Long id, AlterarSenhaUsuarioRequest alterarSenhaUsuarioRequest){

        validaSenhaAtualUsuarioService.validaSenhaAtualUsuario(alterarSenhaUsuarioRequest.getSenhaAtual(), id);

        validaNovaSenhaUsuarioService.validaIgualdadeEntreSenhas(id, alterarSenhaUsuarioRequest.getNovaSenha());

        Usuario usuario = usuarioRepository.findByIdUsuarioAndIsAtivo(id, true).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuário não encontrado."));

        usuario.setSenha(alterarSenhaUsuarioRequest.getNovaSenha());

        usuarioRepository.save(usuario);
    }
}
