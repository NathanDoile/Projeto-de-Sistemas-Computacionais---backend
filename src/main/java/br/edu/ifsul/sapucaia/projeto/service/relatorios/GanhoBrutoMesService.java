package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.GanhoBrutoMesResponse;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
@RequiredArgsConstructor
public class GanhoBrutoMesService {

    private final ReceitaDiariaRepository receitaDiariaRepository;

    public GanhoBrutoMesResponse calcularGanhoBrutoMes(Long idUsuario) {

        LocalDate inicioMes = LocalDate.now()
                .with(TemporalAdjusters.firstDayOfMonth());

        LocalDate fimMes = LocalDate.now()
                .with(TemporalAdjusters.lastDayOfMonth());

        double ganhoBruto = receitaDiariaRepository
                .findByUsuarioIdUsuarioAndDataReceitaBetween(idUsuario, inicioMes, fimMes)
                .stream()
                .mapToDouble(ReceitaDiaria::getValor)
                .sum();

        return GanhoBrutoMesResponse.builder()
                .ganhoBruto(ganhoBruto)
                .build();
    }
}