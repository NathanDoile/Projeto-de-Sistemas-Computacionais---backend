package br.edu.ifsul.sapucaia.projeto.domain.enums;

public enum TipoNotificacao {
    MANUTENCAO("Manutenção"),
    VENCIMENTO("Vencimento");

    private String descricao;

    TipoNotificacao(String descricao){this.descricao = descricao;}

    public String getDescricao() {return descricao;}

    public static TipoNotificacao deTexto(String texto) {
        if (texto == null) return null;

        String tipo = texto.toUpperCase();

        if (tipo.equalsIgnoreCase("manutenção")) {
            return MANUTENCAO;
        }

        return TipoNotificacao.valueOf(tipo);
    }
}
