package br.edu.ifsul.sapucaia.projeto.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class ValidaTipoPeriodoValidator {

    public void porTipo(String tipo) {

        if(!tipo.equalsIgnoreCase("dia")
                && !tipo.equalsIgnoreCase("semana")
                && !tipo.equalsIgnoreCase("mes")){
            throw new ResponseStatusException(BAD_REQUEST, "Tipo dde período inválido. Aceito apenas tipos: dia, semana, mes.");
        }
    }
}
