package br.edu.ifsul.sapucaia.projeto.security.service;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.security.domain.Email;
import br.edu.ifsul.sapucaia.projeto.security.domain.enums.StatusEmail;
import br.edu.ifsul.sapucaia.projeto.security.repository.EmailRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

import static br.edu.ifsul.sapucaia.projeto.security.factory.EmailFactory.criarEmailSemStatus;
import static br.edu.ifsul.sapucaia.projeto.security.factory.SenhaFactory.usuarioComCodigo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnviarEmailServiceTest {

    @InjectMocks
    private EnviarEmailService tested;

    @Mock
    private Environment environment;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Captor
    private ArgumentCaptor<Email> emailCaptor;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    @Test
    @DisplayName("Deve enviar e-mail com sucesso e salvar com status SUCESSO")
    void deveEnviarEmailComSucesso() {
        Email email = criarEmailSemStatus();

        tested.enviar(email);

        verify(javaMailSender).send(messageCaptor.capture());
        verify(emailRepository).save(emailCaptor.capture());

        SimpleMailMessage messageEnviada = messageCaptor.getValue();
        assertEquals(email.getEmailDe(), messageEnviada.getFrom());
        assertEquals(email.getEmailPara(), messageEnviada.getTo()[0]);
        assertEquals(email.getAssunto(), messageEnviada.getSubject());
        assertEquals(email.getMensagem(), messageEnviada.getText());

        Email emailSalvo = emailCaptor.getValue();
        assertEquals(StatusEmail.SUCESSO, emailSalvo.getStatusEmail());
    }

    @Test
    @DisplayName("Deve capturar MailException e salvar e-mail com status ERRO")
    void deveSalvarComStatusErroQuandoOcorrerMailException() {
        Email email = criarEmailSemStatus();

        doThrow(mock(MailException.class)).when(javaMailSender).send(any(SimpleMailMessage.class));

        tested.enviar(email);

        verify(javaMailSender).send(any(SimpleMailMessage.class));
        verify(emailRepository).save(emailCaptor.capture());

        Email emailSalvo = emailCaptor.getValue();
        assertEquals(StatusEmail.ERRO, emailSalvo.getStatusEmail());
    }

    @Test
    @DisplayName("Deve enviar o e-mail de redefinição de senha com sucesso")
    void deveEnviarRedefinirSenhaComSucesso() {
        String emailPara = "usuario@teste.com";
        String assunto = "Redefinição de Senha";
        String conteudo = "Código: 123456";
        String emailDeConfigurado = "sistema@projeto.com";

        Usuario usuario = usuarioComCodigo(null, 0);
        usuario.setNome("João Silva");

        when(environment.getProperty(EnviarEmailService.PATH_ENVIRONMENT_EMAIL)).thenReturn(emailDeConfigurado);
        when(usuarioRepository.findByEmailAndIsAtivo(emailPara, true)).thenReturn(Optional.of(usuario));

        tested.enviarRedefinirSenha(emailPara, assunto, conteudo);

        verify(environment).getProperty(EnviarEmailService.PATH_ENVIRONMENT_EMAIL);
        verify(usuarioRepository).findByEmailAndIsAtivo(emailPara, true);
        verify(javaMailSender).send(any(SimpleMailMessage.class));
        verify(emailRepository).save(emailCaptor.capture());

        Email emailSalvo = emailCaptor.getValue();
        assertEquals(emailDeConfigurado, emailSalvo.getEmailDe());
        assertEquals(emailPara, emailSalvo.getEmailPara());
        assertEquals(assunto, emailSalvo.getAssunto());
        assertEquals(conteudo, emailSalvo.getMensagem());
        assertEquals("João Silva", emailSalvo.getRemetente());
        assertTrue(emailSalvo.isAtivo());
        assertEquals(StatusEmail.SUCESSO, emailSalvo.getStatusEmail());
    }

    @Test
    @DisplayName("Deve disparar RuntimeException em enviarRedefinirSenha quando o usuário não for encontrado")
    void deveLancarRuntimeExceptionQuandoUsuarioNaoForEncontrado() {
        String emailPara = "invalido@teste.com";
        String emailDeConfigurado = "sistema@projeto.com";

        when(environment.getProperty(EnviarEmailService.PATH_ENVIRONMENT_EMAIL)).thenReturn(emailDeConfigurado);
        when(usuarioRepository.findByEmailAndIsAtivo(emailPara, true)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> tested.enviarRedefinirSenha(emailPara, "Assunto", "Conteúdo"));

        assertEquals("Usuário não encontrado para o e-mail: " + emailPara, exception.getMessage());

        verify(environment).getProperty(EnviarEmailService.PATH_ENVIRONMENT_EMAIL);
        verify(usuarioRepository).findByEmailAndIsAtivo(emailPara, true);
        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
        verify(emailRepository, never()).save(any(Email.class));
    }
}