package br.edu.ifsul.sapucaia.projeto.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class ValidadataManutencaoValidator {

    public void dataMenorQueHoje(LocalDate dataManutencao) {

        if(dataManutencao.isAfter(now())){
            throw new ResponseStatusException(BAD_REQUEST, "Data da manutenção não deve ser superior à hoje.");
        }
    }
}
