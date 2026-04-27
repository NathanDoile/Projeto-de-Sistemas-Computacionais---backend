package br.edu.ifsul.sapucaia.projeto.controller;

import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.GanhoBrutoMesResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.GanhoLiquidoMesResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.GastoMesResponse;
import br.edu.ifsul.sapucaia.projeto.controller.response.relatorios.InformacoesDaSemanaResponse;
import br.edu.ifsul.sapucaia.projeto.service.relatorios.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatoriosController {

    private final InformacoesDaSemanaService informacoesDaSemanaService;
    private final ReceitaSemanalService receitaSemanaService;
    private final GanhoLiquidoMesService ganhoLiquidoMesService;
    private final GanhoBrutoMesService ganhoBrutoMesService;
    private final GastoMesService gastoMesService;

    @GetMapping("/informacoes-semana/{idUsuario}")
    public InformacoesDaSemanaResponse  getInformacoesSemana(@PathVariable Long idUsuario){
        return informacoesDaSemanaService.buscarInformacoesDaSemana(idUsuario);
    }

    @GetMapping("/semana/{idUsuario}")
    public InformacoesDaSemanaResponse buscarReceitaDaSemana(@PathVariable Long idUsuario){
        return receitaSemanaService.buscarReceitaDaSemana(idUsuario);
    }

    @GetMapping("/lucro-mes/{idUsuario}")
    public GanhoLiquidoMesResponse getLucroMes(@PathVariable Long idUsuario){
        return ganhoLiquidoMesService.calcularGanhoLiquidoMes(idUsuario);
    }

    @GetMapping("/ganho-bruto-mes/{idUsuario}")
    public GanhoBrutoMesResponse getGanhoBrutoMes(@PathVariable Long idUsuario){
        return ganhoBrutoMesService.calcularGanhoBrutoMes(idUsuario);
    }

    @GetMapping("/gasto-mes/{idUsuario}")
    public GastoMesResponse getGastoMes(@PathVariable Long idUsuario){
        return gastoMesService.calcularGastoMes(idUsuario);
    }

}
