package br.edu.ifsul.sapucaia.projeto.security.factory;

import br.edu.ifsul.sapucaia.projeto.security.domain.Email;
import java.time.LocalDateTime;

public class EmailFactory {

    public static Email criarEmailPadrao() {
        Email email = new Email();
        email.setEmailDe("sistema@projeto.com");
        email.setEmailPara("usuario@teste.com");
        email.setAssunto("Assunto Padrão");
        email.setMensagem("Conteúdo da mensagem de teste");
        email.setAtivo(true);
        email.setEnviadoEm(LocalDateTime.now());
        return email;
    }

    public static Email criarEmailSemStatus() {
        Email email = criarEmailPadrao();
        email.setStatusEmail(null);
        return email;
    }
}