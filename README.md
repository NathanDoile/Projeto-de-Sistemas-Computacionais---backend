# *EXEMPLO DE LISTAGEM DE FUNCIONAIDADES:*
# Projeto de Desenvolvimento de Sistemas

## Funcionalidades

### Rota: `POST /usuarios`
- Cria um usuário;
- Não precisa estar logado para utilizar esse path;
    - `POST /usuarios`
    
        ```json
            {
                  "nome": "Nome Sobrenome",
                  "email": "seuemail@dominio.com",
                  "telefone": "11932564738",
                  "senha": "umasenha",
                  "foto": "url",
                  "professor": "false ou true" 
            }
        ```

### Rota: `GET /usuarios/{email}`
- Retorna o usuário que possui aquele e-mail;
- Qualquer categoria de usuário pode utilizar esse path;