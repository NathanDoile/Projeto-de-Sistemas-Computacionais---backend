package br.edu.ifsul.sapucaia.projeto.validator;

import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoCusto;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class ValidaTipoCustoValidator {

    public void tipoValido(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "Campo tipo é obrigatório.");
        }

        try {
            TipoCusto.deTexto(tipo);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(BAD_REQUEST, "Tipo de custo inválido. Tipos aceitos: Manutenção, Combustível, Seguro, Impostos, Outros.");
        }
    }
}