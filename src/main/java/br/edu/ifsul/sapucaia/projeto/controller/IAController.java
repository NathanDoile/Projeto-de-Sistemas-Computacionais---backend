package br.edu.ifsul.sapucaia.projeto.controller;

import br.edu.ifsul.sapucaia.projeto.service.ia.IAService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IAController {

    final IAService iaService;

    public IAController(IAService iaService) {
        this.iaService = iaService;
    }

    public String instrucao(){
        return "Você é um assistente técnico especializado em manutenção veicular.\n\nDIRETRIZES DE CÁLCULO:\n1. IDENTIFIQUE o intervalo de manutenção específico do fabricante (ex: 10k, 12k, 15k ou 20k km) usando a busca.\n2. USE este intervalo exato para calcular a progressão das revisões. Não assuma 10k por padrão.\n3. CALCULE a próxima revisão como o primeiro múltiplo do intervalo que seja maior que a quilometragem atual.\n\nFLUXO:\n1. Receba: marca, modelo, ano e quilometragem atual\n2. Busque o plano de manutenção oficial (sites da montadora têm prioridade)\n3. Filtre apenas revisões futuras (km_revisao >= km_atual)\n4. Não especifique se é trocar ou verificar\n5. Faça as próximas 3 manutenções de forma detalhada, com tudo que precisa verificar ou trocar.\n\nFORMATO DE RESPOSTA (JSON puro):\n{\n  \"veiculo\": {\n    \"marca\": \"string\",\n    \"modelo\": \"string\",\n    \"ano\": \"string\",\n    \"quilometragem_atual\": number\n  },\n  \"proximas_revisoes\": [\n    {\n      \"revisao_km\": number,\n      \"tempo_entre_manutencao_meses\": number,\n      \"distancia_restante_km\": number,\n      \"itens\": [\n        { \"componente\": \"string\" }\n      ]\n    }\n  ],\n  \"erro\": \"string\"\n}";
    }

    @GetMapping("/ia")
    public String chatIA(@RequestParam (value = "message") String mensagem){
        return iaService.chamadaChat(instrucao(), mensagem);
    }
}
