package br.edu.ifsul.sapucaia.projeto.service.relatorios;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.InformacoesDaSemanaResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.ReceitaDiaria;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.repository.*;
import br.edu.ifsul.sapucaia.projeto.service.validator.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class InformacoesDaSemanaService {
    private final ValidaUsuarioService validaUsuarioService;

    private final CustoRepository custoRepository;

    private final ReceitaDiariaRepository receitaDiariaRepository;

    private final UsuarioRepository usuarioRepository;

    private final ValidaVeiculoService validaVeiculoService;

    public InformacoesDaSemanaResponse buscarInformacoesDaSemana(Long idUsuario){
        validaUsuarioService.porId(idUsuario);

        Usuario usuario = usuarioRepository.findByIdUsuarioAndIsAtivo(idUsuario, true).get();

        LocalDate hoje = LocalDate.now();
        LocalDate inicioSemana = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        List<ReceitaDiaria> receitas = receitaDiariaRepository.findAllByUsuarioIdUsuarioAndIsAtivo(idUsuario, true);
        List<Custo> custos = custoRepository.findAllByVeiculoUsuarioIdUsuarioAndIsAtivoTrue(idUsuario);

        Double ganhoBruto = receitas.stream()
                .filter(r -> !r.getDataReceita().isBefore(inicioSemana) && !r.getDataReceita().isAfter(hoje)).
                mapToDouble(ReceitaDiaria::getValor)
                .sum();

        Double despesas = custos.stream()
                .filter(c -> c.getDataPagamento() != null)
                .filter(c -> !c.getDataPagamento().isBefore(inicioSemana) && !c.getDataPagamento().isAfter(hoje))
                .mapToDouble(Custo::getValor)
                .sum();

        Double lucroLiquido = ganhoBruto - despesas;

        double kmAtual = usuario.getVeiculo().getKmAtual();

        return InformacoesDaSemanaResponse.builder()
                .ganhoBruto(ganhoBruto)
                .despesaTotal(despesas)
                .lucroLiquido(lucroLiquido)
                .kmAtual(kmAtual)
                .build();
    }
}