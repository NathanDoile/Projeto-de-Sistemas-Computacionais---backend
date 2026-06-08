package br.edu.ifsul.sapucaia.projeto.validator;

import br.edu.ifsul.sapucaia.projeto.helper.DateNow;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class ValidaDataManutencaoValidator {

    public void dataMenorQueHoje(LocalDate dataManutencao) {

        if(dataManutencao.isAfter(DateNow.now())){
            throw new ResponseStatusException(BAD_REQUEST, "Data da manutenção não deve ser superior à hoje.");
        }
    }
}
