package br.edu.ifsul.sapucaia.projeto.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static java.time.LocalDate.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class ValidaAnoVeiculoValidator {


    public void anoMenorQueAtual(int ano) {

        if(ano > now().getYear()){
            throw new ResponseStatusException(BAD_REQUEST, "Ano do veículo deve não pode ser maior que o atual.");
        }
    }
}
