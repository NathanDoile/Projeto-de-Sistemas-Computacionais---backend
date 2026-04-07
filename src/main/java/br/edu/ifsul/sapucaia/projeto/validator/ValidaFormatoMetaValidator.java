package br.edu.ifsul.sapucaia.projeto.validator;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ValidaFormatoMetaValidator {

    public void formatoValido(String formato) {

        if (!formato.equalsIgnoreCase("diária") && !formato.equalsIgnoreCase("semanal") && !formato.equalsIgnoreCase("mensal")) {
            throw new ResponseStatusException(BAD_REQUEST, "Formato da meta deve ser diária, semanal ou mensal");
        }
    }
    
}
