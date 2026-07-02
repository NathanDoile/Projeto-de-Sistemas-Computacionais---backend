package br.edu.ifsul.sapucaia.projeto.validator;

import br.edu.ifsul.sapucaia.projeto.helper.DateNow;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class ValidaDataMaiorQueHojeValidator {

    public void naoMaiorQueHoje(LocalDate data){

        if(data.isAfter(DateNow.now())){
            throw new ResponseStatusException(BAD_REQUEST, "A data não deve ser maior que hoje.");
        }
    }

}
