# *LISTAGEM DE FUNCIONALIDADES:*
# Projeto de Desenvolvimento de Sistemas

## Funcionalidades

### Rota: `/usuario`

- **Cria um usuário**
  - `POST /`

    ```json
    {
      "nome": "Primeiro Usuário",
      "email": "testando0@gmail.com",
      "senha": "testando123",
      "telefone": "51999999999"
    }
    ```
- **Login do usuário**
  - `POST /login-usuario`

    ```json
    {
      "email": "adriano@gmail.com",
      "senha":"a4Gj@j21"
    }
    ```

- **Altera a senha de um usuário**
  - Precisa estar logado para utilizar esse path
  - Parâmetros de URL: `id`, é o identificador do usuário
  - `PUT /{id}/alterar-senha`

    ```json
    {
      "senhaAtual": "testando123",
      "novaSenha": "atualizandoSenha123"
    }
    ```

- **Edita o perfil de um usuário**
  - Precisa estar logado para utilizar esse path
  - Parâmetros de URL: `id`, é o identificador do usuário
  - `PUT /{id}/editar-perfil`

    ```json
    {
      "nome": "Nome Atualizado",
      "email": "novo@email.com",
      "telefone": "51999999999"
    }
    ```

    ```json
    {
      "email": "novo@email.com"
    }
    ```

    ```json
    {
      "telefone": "51999999999"
    }
    ```

- **Exclui a conta de um usuário**
  - Precisa estar logado para utilizar esse path
  - Parâmetros de URL: `id`, é o identificador do usuário
  - `DELETE /{id}/excluir-conta`

    ```json
    {
      "senha": "senha123"
    }
    ```

### Rota: `/receita-diaria`

- **Cria uma receita diária**
  - Precisa estar logado para utilizar esse path
  - `POST /`

    ```json
    {
      "dataReceita": "Nome Sobrenome",
      "valor": 200,
      "idUsuario": 1
    }
    ```

### Rota: `/meta`

- **Cria uma meta**
  - Precisa estar logado para utilizar esse path
  - `POST /`

    ```json
    {
      "titulo": "Ingressos para show fim de semana",
      "formato": "Semanal",
      "valor": 300,
      "idUsuario": 1
    }
    ```

### Rota: `/custo`

- **Cria um custo**
  - Precisa estar logado para utilizar esse path
  - `POST /`

    ```json
    {
      "idVeiculo": 1,
      "tipo": "gasolina",
      "valor": 500,
      "dataVencimento": "2026-06-21",
      "dataPagamento": "2026-06-18",
      "descricao": "Custo de gasolina"
    }
    ```  

- **Edita o custo de um usuário**
    - Precisa estar logado para utilizar esse path
    - `PUT /editar`

      ```json
      {
        "idCusto": 1,
        "tipo": "manutencao",
        "valor": 295.00,
        "dataVencimento": "2026-04-23",
        "dataPagamento": "2026-04-15",
        "descricao": "manutencao do carro"
      }
      ``` 


- **Buscar custos em aberto/não pagos**
    - Precisa estar logado para utilizar esse path;
    - Parâmetros de URL: `id`, é o identificador do veículo
    - `GET /{id}/em-aberto`

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
          
### Rota: `/relatorios`
- **Informações de Semana**
  - Precisa estar logado para utilizar esse path;
  - Parâmetros de URL: `id`, é o identificador do usuário
  - `GET /informacoes-semana/{id}`