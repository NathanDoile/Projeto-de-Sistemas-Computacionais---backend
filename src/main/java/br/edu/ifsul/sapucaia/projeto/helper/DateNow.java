package br.edu.ifsul.sapucaia.projeto.helper;

import java.time.LocalDate;
import java.time.ZoneId;

public class DateNow {

    public static LocalDate now(){
        return LocalDate.now(ZoneId.of("America/Sao_Paulo"));
    }
}
