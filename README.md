# *LISTAGEM DE FUNCIONALIDADES:*
# Projeto de Desenvolvimento de Sistemas

## Funcionalidades

### Rota: `/usuario`
- Cria um usuário;
    - `POST /`

        ```json
            {
                "nome": "Primeiro Usuário",
                "email": "testando0@gmail.com",
                "senha": "testando123",
                "telefone": "51999999999"
            }
        ```

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

### Rota: `/manutencao`
- Cria uma manutencao;
    - Precisa estar logado para utilizar esse path;
        - `POST /`

            ```json
              {
                      "tipo":"preventiva",
                      "dataManutencao": "2026-04-02",
                      "descricao":"fiz uma manutencao",
                      "idVeiculo":1,
                      "idCusto":1
              }
            ``` 