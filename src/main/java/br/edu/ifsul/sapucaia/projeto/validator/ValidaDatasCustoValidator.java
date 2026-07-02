package br.edu.ifsul.sapucaia.projeto.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class ValidaDatasCustoValidator {


    public void ambasNull(LocalDate dataVencimento, LocalDate dataPagamento) {

        if(isNull(dataVencimento) && isNull(dataPagamento)){
            throw new ResponseStatusException(BAD_REQUEST, "Informe uma das datas: data de vencimento ou data de pagamento.");
        }
    }
}
