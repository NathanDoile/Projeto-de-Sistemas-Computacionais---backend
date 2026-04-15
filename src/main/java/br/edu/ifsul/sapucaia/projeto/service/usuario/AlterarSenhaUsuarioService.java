package br.edu.ifsul.sapucaia.projeto.service.usuario;

import br.edu.ifsul.sapucaia.projeto.controller.request.usuario.AlterarSenhaUsuarioRequest;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaNovaSenhaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaSenhaAtualUsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlterarSenhaUsuarioService {
    private final UsuarioRepository usuarioRepository;

    private final ValidaSenhaAtualUsuarioService validaSenhaAtualUsuarioService;

    private final ValidaNovaSenhaUsuarioService validaNovaSenhaUsuarioService;

    @Transactional
    public void     alterarSenhaUsuario(Long id, AlterarSenhaUsuarioRequest alterarSenhaUsuarioRequest){
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        validaSenhaAtualUsuarioService.validaSenhaAtualUsuario(alterarSenhaUsuarioRequest.getSenhaAtual(), usuario.getSenha());

        validaNovaSenhaUsuarioService.validaIgualdadeEntreSenhas(usuario.getSenha(), alterarSenhaUsuarioRequest.getNovaSenha());

        usuario.setSenha(alterarSenhaUsuarioRequest.getNovaSenha());

        usuarioRepository.save(usuario);
    }
}
