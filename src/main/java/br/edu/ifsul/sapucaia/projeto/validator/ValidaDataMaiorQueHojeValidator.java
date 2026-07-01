package br.edu.ifsul.sapucaia.projeto.validator;

import br.edu.ifsul.sapucaia.projeto.helper.DateNow;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ValidaDataMaiorQueHojeValidator {

    public void naoMaiorQueHoje(LocalDate data){

        if(data.isAfter(DateNow.now())){
            throw new ResponseStatusException(BAD_REQUEST, "A data deve não deve ser maior que hoje.");
        }
    }

}
