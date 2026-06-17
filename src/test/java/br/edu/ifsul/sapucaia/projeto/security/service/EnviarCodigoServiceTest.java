package br.edu.ifsul.sapucaia.projeto.security.service;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.controller.request.EnviarCodigoRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static br.edu.ifsul.sapucaia.projeto.security.factory.SenhaFactory.enviarCodigoRequest;
import static br.edu.ifsul.sapucaia.projeto.security.factory.SenhaFactory.usuarioComCodigo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnviarCodigoServiceTest {

    @InjectMocks
    private EnviarCodigoService tested;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EnviarEmailService enviarEmailService;

    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    @Test
    @DisplayName("Deve gerar o código, salvar o usuário e enviar o e-mail com sucesso")
    void deveEnviarCodigoComSucesso() {
        EnviarCodigoRequest request = enviarCodigoRequest();
        Usuario usuario = usuarioComCodigo(null, -1);

        when(usuarioRepository.findByEmailAndIsAtivo(request.getEmail(), true)).thenReturn(Optional.of(usuario));

        tested.enviarCodigo(request);

        verify(usuarioRepository).findByEmailAndIsAtivo(request.getEmail(), true);
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario usuarioSalvo = usuarioCaptor.getValue();
        String codigoGerado = usuarioSalvo.getCodigoRedefinirSenha();

        assertNotNull(codigoGerado);
        assertEquals(8, codigoGerado.length());
        assertEquals(0, usuarioSalvo.getTentativasRedefinirSenha());

        String conteudo = "Olá,\n\n" +
                "Recebemos uma solicitação para redefinir a senha da sua conta. " +
                "Utilize o código abaixo para prosseguir com a redefinição:\n\n" +
                "Código: " + codigoGerado + "\n\n" +
                "Se você não solicitou essa alteração, por favor ignore este e-mail.\n\n" +
                "Atenciosamente,\n" +
                "Equipe de Suporte";

        verify(enviarEmailService).enviarRedefinirSenha(request.getEmail(), "Redefinição de Senha", conteudo);
    }

    @Test
    @DisplayName("Deve disparar NOT_FOUND quando o e-mail informado não existir")
    void deveDispararNotFoundQuandoEmailNaoExistir() {
        EnviarCodigoRequest request = enviarCodigoRequest();

        when(usuarioRepository.findByEmailAndIsAtivo(request.getEmail(), true)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
                () -> tested.enviarCodigo(request));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("E-mail não encontrado.", exception.getReason());

        verify(usuarioRepository).findByEmailAndIsAtivo(request.getEmail(), true);
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(enviarEmailService, never()).enviarRedefinirSenha(anyString(), anyString(), anyString());
    }
}