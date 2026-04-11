package br.edu.ifsul.sapucaia.projeto.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class ValidaTipoManutencaoValidator {

    public void tipoValido(String tipo) {

        if(!tipo.equalsIgnoreCase("preventiva") && !tipo.equalsIgnoreCase("corretiva")
                && !tipo.equalsIgnoreCase("preditiva")){
            throw new ResponseStatusException(BAD_REQUEST, "Tipo de manutenção inválida.");
        }
    }
}
