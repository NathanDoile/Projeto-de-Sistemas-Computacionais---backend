package br.edu.ifsul.sapucaia.projeto.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class ValidaDataRelatorioFinanceiroPdfValidator {
    public void naoMaiorQueHoje(LocalDate dataReferencia) {

        if(dataReferencia.isAfter(LocalDate.now())){
            throw new ResponseStatusException(BAD_REQUEST, "Data de referência não pode ser superior à hoje");
        }
    }
}
