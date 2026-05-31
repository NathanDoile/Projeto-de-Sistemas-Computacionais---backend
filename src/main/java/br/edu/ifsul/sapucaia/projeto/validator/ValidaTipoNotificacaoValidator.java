package br.edu.ifsul.sapucaia.projeto.validator;

import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoNotificacao;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class ValidaTipoNotificacaoValidator {

    public void tipoValido(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "O tipo de notificação é obrigatório.");
        }

        try {
            TipoNotificacao.deTexto(tipo);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(BAD_REQUEST, "Tipo de Notificação inválido. Tipos aceitos: Manutenção, Vencimento.");
        }
    }

}
