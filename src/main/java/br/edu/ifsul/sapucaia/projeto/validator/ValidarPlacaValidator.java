package br.edu.ifsul.sapucaia.projeto.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static java.lang.Character.isLetter;
import static java.lang.Character.isLetterOrDigit;
import static java.lang.Character.isDigit;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class ValidarPlacaValidator {


    public void formatoValido(String placa) {

        for(int i = 0; i < 3; i++){

            if(!isLetter(placa.charAt(i))){
                throw new ResponseStatusException(BAD_REQUEST, "Formato de placa incorreto.");
            }
        }

        if(!isDigit(placa.charAt(3))){
            throw new ResponseStatusException(BAD_REQUEST, "Formato de placa incorreto.");
        }

        if(!isLetterOrDigit(placa.charAt(4))){
            throw new ResponseStatusException(BAD_REQUEST, "Formato de placa incorreto.");
        }

        for(int i = 5; i < 7; i++){

            if(!isDigit(placa.charAt(i))){
                throw new ResponseStatusException(BAD_REQUEST, "Formato de placa incorreto.");
            }
        }
    }
}

