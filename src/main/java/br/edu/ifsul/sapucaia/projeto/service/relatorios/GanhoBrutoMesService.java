package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.GanhoBrutoMesResponse;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
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
    private final ValidaUsuarioService validaUsuarioService;

    public GanhoBrutoMesResponse calcularGanhoBrutoMes(Long idUsuario) {

        Usuario usuario = validaUsuarioService.buscarUsuarioPorId(idUsuario);

        LocalDate inicioMes = LocalDate.now()
                .with(TemporalAdjusters.firstDayOfMonth());

        LocalDate fimMes = LocalDate.now()
                .with(TemporalAdjusters.lastDayOfMonth());

        double ganhoBruto = receitaDiariaRepository
                .findByUsuarioAndDataBetween(usuario, inicioMes, fimMes)
                .stream()
                .mapToDouble(ReceitaDiaria::getValor)
                .sum();

        return GanhoBrutoMesResponse.builder()
                .ganhoBruto(ganhoBruto)
                .build();
    }
}