package br.edu.ifsul.sapucaia.projeto.domain.enums;

import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public enum PeriodoRelatorioFinanceiro {
    SEMANAL, MENSAL, ANUAL;

    public static PeriodoRelatorioFinanceiro deTexto(String texto){
        if(texto == null) throw new ResponseStatusException(BAD_REQUEST, "Período não informado");

        String t = texto.toUpperCase();

        if(t.equalsIgnoreCase("semanal")){
            return SEMANAL;
        }
        else if(t.equalsIgnoreCase("mensal")){
            return MENSAL;
        }
        else if(t.equalsIgnoreCase("anual")){
            return ANUAL;
        }
        else{
            throw new ResponseStatusException(BAD_REQUEST, "O período deve ser semanal, mensal ou anual");
        }
    }
}
