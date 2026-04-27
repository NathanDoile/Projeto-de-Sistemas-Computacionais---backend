package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.GanhoLiquidoMesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
@RequiredArgsConstructor

    public class GanhoLiquidoMesService {

        private final ReceitaDiariaRepository receitaDiariaRepository;
        private final CustoRepository custoRepository;
        private final ValidaUsuarioService validaUsuarioService;

        public GanhoLiquidoMesResponse calcularGanhoLiquidoMes(Long idUsuario) {

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

            double despesaTotal = custoRepository
                    .findByUsuarioAndDataBetween(usuario, inicioMes, fimMes)
                    .stream()
                    .mapToDouble(Custo::getValor)
                    .sum();

            double lucroLiquido = ganhoBruto - despesaTotal;

            return GanhoLiquidoMesResponse.builder()
                    .lucroLiquido(lucroLiquido)
                    .build();
        }
    }