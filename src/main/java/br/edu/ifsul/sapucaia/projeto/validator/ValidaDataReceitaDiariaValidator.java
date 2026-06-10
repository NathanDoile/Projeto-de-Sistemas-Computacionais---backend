package br.edu.ifsul.sapucaia.projeto.validator;

import br.edu.ifsul.sapucaia.projeto.helper.DateNow;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class ValidaDataReceitaDiariaValidator {

    public void naoMaiorQueHoje(LocalDate dataReceita) {

        if(dataReceita.isAfter(DateNow.now())){
            throw new ResponseStatusException(BAD_REQUEST, "Data da sua receita não pode ser superior à hoje");
        }
    }
}
