package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.GastoMesResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
@RequiredArgsConstructor
public class GastoMesService {

    private final CustoRepository custoRepository;
    private final ValidaUsuarioService validaUsuarioService;

    public GastoMesResponse calcularGastoMes(Long idUsuario) {

        Usuario usuario = validaUsuarioService.buscarUsuarioPorId(idUsuario);

        LocalDate inicioMes = LocalDate.now()
                .with(TemporalAdjusters.firstDayOfMonth());

        LocalDate fimMes = LocalDate.now()
                .with(TemporalAdjusters.lastDayOfMonth());

        double despesaTotal = custoRepository
                .findByUsuarioAndDataBetween(usuario, inicioMes, fimMes)
                .stream()
                .mapToDouble(Custo::getValor)
                .sum();

        return GastoMesResponse.builder()
                .despesaTotal(despesaTotal)
                .build();
    }
}