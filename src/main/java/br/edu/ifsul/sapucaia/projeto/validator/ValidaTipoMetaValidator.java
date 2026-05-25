package br.edu.ifsul.sapucaia.projeto.validator;

import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoMeta;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ValidaTipoMetaValidator {

    public void tipoValido(String tipo){

        try{
            TipoMeta.deTexto(tipo);
        }
        catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo da meta deve ser receita ou custo"
            );
        }
    }
}