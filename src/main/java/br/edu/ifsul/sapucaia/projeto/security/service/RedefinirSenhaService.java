package br.edu.ifsul.sapucaia.projeto.security.service;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.controller.request.EnviarCodigoRequest;
import br.edu.ifsul.sapucaia.projeto.security.controller.request.RedefinirSenhaRequest;
import br.edu.ifsul.sapucaia.projeto.security.service.validator.ValidaCodigoValidator;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaNovaSenhaUsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RedefinirSenhaService {

    private final PasswordEncoder passwordEncoder;

    private final UsuarioRepository usuarioRepository;

    private final EnviarEmailService enviarEmailService;

    private final ValidaCodigoValidator validaCodigoValidator;

    private final ValidaNovaSenhaUsuarioService validaNovaSenhaUsuarioService;

    @Transactional
    public void enviarCodigo(EnviarCodigoRequest request) {

        Usuario usuario = usuarioRepository.findByEmailAndIsAtivo(request.getEmail(), true)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "E-mail não encontrado."));

        String codigo = gerarCodigo();

        usuario.setCodigoRedefinirSenha(codigo);
        usuario.setTentativasRedefinirSenha(0);
        usuarioRepository.save(usuario);

        String conteudo = "Olá,\n\n" +
            "Recebemos uma solicitação para redefinir a senha da sua conta. " +
            "Utilize o código abaixo para prosseguir com a redefinição:\n\n" +
            "Código: " + codigo + "\n\n" +
            "Se você não solicitou essa alteração, por favor ignore este e-mail.\n\n" +
            "Atenciosamente,\n" +
            "Equipe de Suporte";

        enviarEmailService.enviarRedefinirSenha(request.getEmail(), "Redefinição de Senha", conteudo);
    }

    @Transactional
    public void redefinirSenha(RedefinirSenhaRequest request) {

        Usuario usuario = usuarioRepository.findByEmailAndIsAtivo(request.getEmail(), true)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "E-mail não encontrado."));

        validaCodigoValidator.validarCodigo(usuario, request.getCodigo());
        validaNovaSenhaUsuarioService.validaIgualdadeEntreSenhas(usuario.getIdUsuario(), request.getSenha());

        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setCodigoRedefinirSenha(null);
        usuario.setTentativasRedefinirSenha(0);
        usuarioRepository.save(usuario);
    }

    private String gerarCodigo() {

        SecureRandom random = new SecureRandom();

        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            
        StringBuilder codigo = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(caracteres.length());
            codigo.append(caracteres.charAt(index));
        }

        return codigo.toString();
    }
}