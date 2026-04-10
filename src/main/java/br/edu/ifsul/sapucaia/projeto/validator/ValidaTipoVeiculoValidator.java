package br.edu.ifsul.sapucaia.projeto.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class ValidaTipoVeiculoValidator {

    public void tipoAceito(String tipo) {

        if(!tipo.equalsIgnoreCase("carro") && !tipo.equalsIgnoreCase("motocicleta")){
            throw new ResponseStatusException(BAD_REQUEST, "Tipo de veículo não aceito.");
        }
    }
}
