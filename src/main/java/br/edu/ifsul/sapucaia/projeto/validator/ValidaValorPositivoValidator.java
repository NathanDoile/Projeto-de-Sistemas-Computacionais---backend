package br.edu.ifsul.sapucaia.projeto.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class ValidaValorPositivoValidator {

    public void isPositivo(Double valor) {

        if(valor <= 0){
            throw new ResponseStatusException(BAD_REQUEST, "Valor deve ser maior que zero.");
        }
    }

}
