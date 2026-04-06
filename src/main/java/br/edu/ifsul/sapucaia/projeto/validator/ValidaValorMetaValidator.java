package br.edu.ifsul.sapucaia.projeto.validator;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ValidaValorMetaValidator {
    
    public void isPositivo(Double valor) {

        if(valor <= 0){
            throw new ResponseStatusException(BAD_REQUEST, "Valor da sua meta deve ser maior que zero.");
        }
    }
    
}