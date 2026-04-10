# *LISTAGEM DE FUNCIONAIDADES:*
# Projeto de Desenvolvimento de Sistemas

## Funcionalidades

### Rota: `/receita-diaria`
- Cria uma receita diária;
- Precisa estar logado para utilizar esse path;
    - `POST /`
    
        ```json
            {
                  "dataReceita": "Nome Sobrenome",
                  "valor": 200,
                  "idUsuario": 1
            }
        ```

### Rota: `/meta`
- Cria uma meta;
- Precisa estar logado para utilizar esse path;
    - `POST /`

        ```json
            {
                "titulo":"Ingressos para show fim de semana",
                "formato":"Semanal",
                "valor":300,
                "idUsuario":1
            }
        ```    

### Rota: `/custo`
- Cria um custo;
  - Precisa estar logado para utilizar esse path;
      - `POST /`

          ```json
              {
                      "idVeiculo":1,
                      "tipo":"gasolina",
                      "valor":500,
                      "dataVencimento":"2026-06-21",
                      "dataPagamento":"2026-06-18",
                      "descricao":"Custo de gasolina"
              }
          ```   

### Rota: `/veiculo`
- Cria um veiculo;
    - Precisa estar logado para utilizar esse path;
        - `POST /`

            ```json
              {
                      "marca":"VW",
                      "modelo":"ONIX",
                      "placa":"ABC1A23",
                      "tipo":"carro",
                      "ano":2023,
                      "cor":"azul",
                      "kmAtual":23589,
                      "idUsuario":1
              }
            ```   