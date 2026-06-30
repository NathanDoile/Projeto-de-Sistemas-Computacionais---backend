package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.InformacoesDaSemanaResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Veiculo;
import br.edu.ifsul.sapucaia.projeto.helper.DateNow;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.ReceitaDiariaRepository;
import br.edu.ifsul.sapucaia.projeto.repository.VeiculoRepository;
import br.edu.ifsul.sapucaia.projeto.security.UsuarioSecurity;
import br.edu.ifsul.sapucaia.projeto.security.service.UsuarioAutenticadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InformacoesDaSemanaService {

    private final CustoRepository custoRepository;

    private final ReceitaDiariaRepository receitaDiariaRepository;

    private final UsuarioAutenticadoService usuarioAutenticadoService;

    private final VeiculoRepository veiculoRepository;

    public InformacoesDaSemanaResponse buscarInformacoesDaSemana(){

        UsuarioSecurity usuario = usuarioAutenticadoService.getUser();

        Veiculo veiculo = veiculoRepository.findByIdVeiculo(usuario.getId());

        LocalDate hoje = DateNow.now();
        LocalDate inicioSemana = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        List<ReceitaDiaria> receitas = receitaDiariaRepository.findByUsuarioIdUsuarioAndDataReceitaBetween(usuario.getId(), inicioSemana, hoje);
        List<Custo> custos = custoRepository.findByVeiculoIdVeiculoAndDataPagamentoBetween(veiculo.getIdVeiculo(), inicioSemana, hoje);

        double ganhoBruto = receitas.stream()
                .mapToDouble(ReceitaDiaria::getValor)
                .sum();

        double despesas = custos.stream()
                .mapToDouble(Custo::getValor)
                .sum();

        double lucroLiquido = ganhoBruto - despesas;

        double kmAtual = veiculo.getKmAtual();

        return InformacoesDaSemanaResponse.builder()
                .ganhoBruto(ganhoBruto)
                .despesaTotal(despesas)
                .lucroLiquido(lucroLiquido)
                .kmAtual(kmAtual)
                .build();
    }
}