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
class PeriodoDataHelperTest {

    @InjectMocks
    private PeriodoDataHelper tested;

    @Test
    @DisplayName("Deve retornar as datas corretamente se tipo for valido")
    void deveRetornarDatasCorretamenteSeTipoValido(){

        String tipoI = "dia";
        String tipoII = "semana";
        String tipoIII = "mes";
        String tipoIV = "ano";

        String dataBase = "2026-05-20";

        PeriodoData responseI = tested.calcularData(tipoI, null);
        PeriodoData responseII = tested.calcularData(tipoII, dataBase);
        PeriodoData responseIII = tested.calcularData(tipoIII, dataBase);
        PeriodoData responseIV= tested.calcularData(tipoIV, dataBase);

        assertEquals(LocalDate.now(), responseI.dataInicio());
        assertEquals(LocalDate.now(), responseI.dataFim());

        assertEquals(LocalDate.of(2026, 5, 18), responseII.dataInicio());
        assertEquals(LocalDate.of(2026, 5, 24), responseII.dataFim());

        assertEquals(LocalDate.of(2026, 5, 1), responseIII.dataInicio());
        assertEquals(LocalDate.of(2026, 5, 31), responseIII.dataFim());

        assertEquals(LocalDate.of(2026, 1, 1), responseIV.dataInicio());
        assertEquals(LocalDate.of(2026, 12, 31), responseIV.dataFim());
    }
}
