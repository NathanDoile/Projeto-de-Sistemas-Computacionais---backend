package br.edu.ifsul.sapucaia.projeto.validator;

import br.edu.ifsul.sapucaia.projeto.helper.DateNow;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class ValidaAnoVeiculoValidator {


    public void anoMenorQueAtual(int ano) {

        if(ano > DateNow.now().getYear()){
            throw new ResponseStatusException(BAD_REQUEST, "Ano do veículo deve não pode ser maior que o atual.");
        }
    }
}
