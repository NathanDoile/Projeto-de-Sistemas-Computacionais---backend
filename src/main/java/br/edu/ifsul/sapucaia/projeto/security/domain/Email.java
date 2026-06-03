package br.edu.ifsul.sapucaia.projeto.security.domain;

import br.edu.ifsul.sapucaia.projeto.security.domain.enums.StatusEmail;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Email {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long idEmail;

    private String remetente;

    private String emailDe;

    private String emailPara;

    private String assunto;

    private String mensagem;

    private LocalDateTime enviadoEm;

    @Enumerated(STRING)
    private StatusEmail statusEmail;

}