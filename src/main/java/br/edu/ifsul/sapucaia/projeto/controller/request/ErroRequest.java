package br.edu.ifsul.sapucaia.projeto.controller.request;

public class ErroRequest {

    private ErroRequest(){}

    public static final String CAMPO_OBRIGATORIO = "é obrigatório.";

    public static final String CAMPO_POSITIVO = "deve ser maior que zero.";

    public static final String CAMPO_EMAIL = "deve ser um e-mail válido.";

    public static final String CAMPO_PLACA = "deve ter 7 caracteres, sem hífen.";

    public static final int CAMPO_TAMANHO_MAXIMO_MINIMO_PLACA = 7;

    public static final String CAMPO_CODIGO_RECUPERAR_SENHA = "deve conter 8 caracteres.";

    public static final int CAMPO_TAMANHO_MAXIMO_MINIMO_CODIGO_RCUPERAR_SENHA = 8;
}
