package br.edu.ifsul.sapucaia.projeto.service.relatorios;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.GastosPorCategoriaDoMesResponse;
import br.edu.ifsul.sapucaia.projeto.domain.Custo;
import br.edu.ifsul.sapucaia.projeto.domain.Usuario;
import br.edu.ifsul.sapucaia.projeto.domain.enums.TipoCusto;
import br.edu.ifsul.sapucaia.projeto.repository.CustoRepository;
import br.edu.ifsul.sapucaia.projeto.repository.UsuarioRepository;
import br.edu.ifsul.sapucaia.projeto.service.validator.ValidaUsuarioService;

@Service
@RequiredArgsConstructor
public class GastosPorCategoriaDoMesService {

    private final ValidaUsuarioService validaUsuarioService;
    private final CustoRepository custoRepository;
    private final UsuarioRepository usuarioRepository;

    public GastosPorCategoriaDoMesResponse calcularGastosPorCategoriaDoMes(Long idUsuario) {
        
        validaUsuarioService.porId(idUsuario);
        
        Usuario usuario = usuarioRepository.findByIdUsuarioAndIsAtivo(idUsuario, true).get();

        LocalDate inicioMes = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate fimMes = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        List<Custo> custos = custoRepository.findByVeiculoIdVeiculoAndDataPagamentoBetween(usuario.getVeiculo().getIdVeiculo(), inicioMes, fimMes);

        Map<TipoCusto, Double> gastosMap = custos.stream()
                .collect(Collectors.groupingBy(
                        Custo::getTipo, 
                        Collectors.summingDouble(Custo::getValor)
                ));

        return GastosPorCategoriaDoMesResponse.builder()
                .manutencao(gastosMap.getOrDefault(TipoCusto.MANUTENCAO, 0.0))
                .combustivel(gastosMap.getOrDefault(TipoCusto.COMBUSTIVEL, 0.0))
                .seguro(gastosMap.getOrDefault(TipoCusto.SEGURO, 0.0))
                .impostos(gastosMap.getOrDefault(TipoCusto.IMPOSTOS, 0.0))
                .outros(gastosMap.getOrDefault(TipoCusto.OUTROS, 0.0))
                .build();
    }
}