package br.edu.ifsul.sapucaia.projeto.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class ValidaKmAtualizadoVeiculoValidator {

    public void maiorQueAtual(int kmAtualizado, int kmAtual) {

        if(kmAtualizado <= kmAtual){
            throw new ResponseStatusException(BAD_REQUEST, "O quilômetro atualizado deve ser maior que o registrado anteriormente.");
        }
    }
}
