package br.edu.ifsul.sapucaia.projeto.helper;

import br.edu.ifsul.sapucaia.projeto.helper.record.PeriodoData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PeriodoDataHelperTest {

    @InjectMocks
    private PeriodoDataHelper tested;

    @Test
    @DisplayName("Deve retornar as datas corretamente se tipo for dia")
    void deveRetornarDatasCorretamenteSeTipoDia(){

        String tipo = "dia";
        String dataBase = "2026-05-20";

        PeriodoData response = tested.calcularData(tipo, dataBase);

        assertEquals(LocalDate.of(2026, 5, 20), response.dataInicio());
        assertEquals(LocalDate.of(2026, 5, 20), response.dataFim());
    }

    @Test
    @DisplayName("Deve retornar as datas corretamente se tipo for semana")
    void deveRetornarDatasCorretamenteSeTipoSemana(){

        String tipo = "semana";
        String dataBase = "2026-05-20";

        PeriodoData response = tested.calcularData(tipo, dataBase);

        assertEquals(LocalDate.of(2026, 5, 18), response.dataInicio());
        assertEquals(LocalDate.of(2026, 5, 24), response.dataFim());
    }

    @Test
    @DisplayName("Deve retornar as datas corretamente se tipo for mes")
    void deveRetornarDatasCorretamenteSeTipoMes(){

        String tipo = "mes";
        String dataBase = "2026-05-20";

        PeriodoData response = tested.calcularData(tipo, dataBase);

        assertEquals(LocalDate.of(2026, 5, 1), response.dataInicio());
        assertEquals(LocalDate.of(2026, 5, 31), response.dataFim());
    }
}
