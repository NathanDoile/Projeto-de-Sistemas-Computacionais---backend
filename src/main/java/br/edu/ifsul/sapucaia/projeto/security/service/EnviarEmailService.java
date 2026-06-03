package br.edu.ifsul.sapucaia.projeto.security.service;

import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.security.domain.Email;
import br.edu.ifsul.sapucaia.projeto.security.repository.EmailRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static java.time.LocalDateTime.now;
import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class EnviarEmailService {

    public static final String PATH_ENVIRONMENT_EMAIL = "spring.mail.username";

    private final Environment environment;

    private final JavaMailSender javaMailSender;

    private final EmailRepository emailRepository;

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public void enviar(Email email) {

        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(email.getEmailDe());
            message.setTo(email.getEmailPara());
            message.setSubject(email.getAssunto());
            message.setText(email.getMensagem());

            javaMailSender.send(message);

            email.setStatusEmail(br.edu.ifsul.sapucaia.projeto.security.domain.enums.StatusEmail.SUCESSO);
        } catch (MailException e) {
            email.setStatusEmail(br.edu.ifsul.sapucaia.projeto.security.domain.enums.StatusEmail.ERRO);
        } finally {
            emailRepository.save(email);
        }
    }

    @Transactional
    public void enviarRedefinirSenha(String emailPara, String assuntoPadrao, String conteudo) {
        
        Email email = new Email();
        email.setAssunto(assuntoPadrao);
        email.setMensagem(conteudo);
        email.setEmailDe(requireNonNull(environment.getProperty(PATH_ENVIRONMENT_EMAIL)));
        email.setEmailPara(emailPara);
        email.setEnviadoEm(now());

        Usuario usuario = usuarioRepository.findByEmailAndIsAtivo(emailPara, true)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para o e-mail: " + emailPara));

        email.setRemetente(usuario.getNome());
        email.setAtivo(true);

        enviar(email);
    }
}
