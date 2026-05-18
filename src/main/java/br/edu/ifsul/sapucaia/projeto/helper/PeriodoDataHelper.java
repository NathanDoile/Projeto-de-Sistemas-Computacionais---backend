package br.edu.ifsul.sapucaia.projeto.helper;

import br.edu.ifsul.sapucaia.projeto.helper.record.PeriodoData;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static java.time.LocalDate.now;
import static java.time.LocalDate.parse;

@Component
public class PeriodoDataHelper {

    public PeriodoData calcularData(String tipo, String dataBase){

        LocalDate dataBaseDate = (dataBase != null)
                ? parse(dataBase)
                : now();

        LocalDate dataInicio;
        LocalDate dataFim;

        if(tipo.equalsIgnoreCase("dia")){
            dataInicio = dataBaseDate;
            dataFim = dataBaseDate;
        }
        else if(tipo.equalsIgnoreCase("semana")){
            LocalDate inicioSemana = dataBaseDate.with(DayOfWeek.MONDAY);
            LocalDate fimSemana = dataBaseDate.with(DayOfWeek.SUNDAY);

            dataInicio = inicioSemana;
            dataFim = fimSemana;
        }
        else{
            LocalDate inicioMes = dataBaseDate.with(TemporalAdjusters.firstDayOfMonth());
            LocalDate fimMes = dataBaseDate.with(TemporalAdjusters.lastDayOfMonth());

            dataInicio = inicioMes;
            dataFim = fimMes;
        }

        return new PeriodoData(dataInicio, dataFim);
    }
}
