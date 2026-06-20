# *LISTAGEM DE FUNCIONALIDADES:*
# Projeto de Desenvolvimento de Sistemas

## Funcionalidades

### Rota Principal:

- **Login do usuário**
    - `POST /login`

      ```Basic Auth
        "email": "adriano@gmail.com",
        "password": "123"
      ```

- **Logout do usuário**
    - `POST /logout`

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
- **Ativa/Desativa notificações**
  - Precisa estar logado para utilizar esse path
  - Altera a configuração de notificações do usuário
  - Parâmetros de URL: `id`, é o identificador do usuário
  - `PUT /notificacoes/{id}`

    ```json
    {
       "notificacao": "Vencimento"
    }
    ```

### Rota: `/security`

- **Enviar código de redefinição de senha**
  - Não precisa estar logado para utilizar esse path
  - `POST /enviar-codigo`

    ```json
    {
      "email": "usuario@email.com"
    }
    ```

- **Validar código de redefinição de senha**
  - Não precisa estar logado para utilizar esse path
  - `POST /validar-codigo`

    ```json
    {
      "email": "usuario@email.com",
      "codigo": "ABC12345"
    }
    ```

- **Redefinir senha**
  - Não precisa estar logado para utilizar esse path
  - `PUT /redefinir-senha`

    ```json
    {
      "email": "usuario@email.com",
      "codigo": "ABC12345",
      "senha": "novaSenha@123"
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

- **Buscar metas**
    - Precisa estar logado para utilizar esse path
    - `GET /`

- **Excluir meta**
    - Precisa estar logado para utilizar esse path
    - `DELETE /{id}`
    - Parâmetros de URL: `id`, é o identificador da meta

### Rota: `/custo`

- **Cria um custo**
  - Precisa estar logado para utilizar esse path
  - `POST /`

    ```json
    {
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
    - Parâmetros de URL: `page`, é qual página de custos quer buscar;
    - Parâmetros de URL: `size`, é o tamanho da lista de custos que quer mostrar em cada página;
    - `GET /{id}/em-aberto?page=0$size=2`

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

- **Retornar dados do veículo**
  - Precisa estar logado para utilizar esse path;
  - `GET`

- **Obter informações de manutenção do veículo**
  - Precisa estar logado para utilizar esse path
  - `GET /informacoes-manutencao`

    Retorna:
    ```json
    {
      "totalManutencoesPreventivas": 2,
      "totalManutencoesCorretivas": 1,
      "totalManutencoesPreditivas": 0,
      "valorTotalPreventivas": 300.00,
      "valorTotalCorretivas": 500.00,
      "valorTotalPreditivas": 0.00,
      "mediaPrecoManutencao": 266.67,
      "valorCustoPorKmRodado": 0.012
    }
    ```


- Atualizar quilometragem do veículo
  - Precisa estar logado para utilizar esse path;
  - `PUT /atualizar-km`
      
      ```json
            {
                    "idUsuario":1,
                    "kmAtualizado":45300
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
                      "valor":300
              }
            ```
          
- Lista as manutenções:
  - Precisa estar logado para utilizar essa path;
  - Parâmetros de URL: `page`, é qual página de custos quer buscar;
  - Parâmetros de URL: `size`, é o tamanho da lista de custos que quer mostrar em cada página;
    - `GET ?page=1$size=10`
          
### Rota: `/relatorios`
- **Informações de Semana**
  - Precisa estar logado para utilizar esse path;
  - Parâmetros de URL: `id`, é o identificador do usuário
  - `GET /informacoes-semana/{id}`

- **Receita da Semana**
  - Precisa estar logado para utilizar esse path;
  - Parâmetros de URL: `id`, é o identificador do usuário
  - `GET /receita-semana/{idUsuario}`

- **Resumo Financeiro**
  - Precisa estar logado para utilizar esse path;
  - Parâmetros de URL: `id`, é o identificador do usuário
  - Parâmetros de query: tipo (dia | semana | mes) e data (opcional, formato YYYY-MM-DD)
  - `GET /resumo-financeiro/{idUsuario}`  

- **Gasto por categoria**
  - Precisa estar logado para utilizar esse path;
  - Parâmetros de query: tipo (dia | semana | mes | ano) e dataBase (formato YYYY-MM-DD)
  - `GET /gastos-categoria-dia`

- **Últimas transações**
  - Precisa estar logado para utilizar esse path;
  - `GET /ultimas-transacoes`

- **Exportar Relatório de Manutenções**
  - Precisa estar logado para utilizar esse path;
  - Parâmetros de query: idVeiculo, tipoPeriodo(semana, mes e ano) e dataReferencia(Formato YYYY-MM-DD)
  - `GET /exportar/manutencoes`

- **Exportar Relatório Financeiro**
  - Precisa estar logado para utilizar esse path;
  - Parâmetros de query: idUsuario, dataReferencia(Formato YYYY-MM-DD) e periodo(semanal, mensal e anual)
  - `GET /exportar/financeiro`

- **Verificar pendências do usuário com o sistema**
  - Precisa estar logado para utilizar esse path;
  - `GET /pendencias`