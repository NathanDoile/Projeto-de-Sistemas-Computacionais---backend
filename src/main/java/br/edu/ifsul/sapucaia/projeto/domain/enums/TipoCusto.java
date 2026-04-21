package br.edu.ifsul.sapucaia.projeto.domain.enums;

public enum TipoCusto {
    MANUTENCAO("Manutenção"),
    COMBUSTIVEL("Combustível"),
    SEGURO("Seguro"),
    IMPOSTOS("Impostos"),
    OUTROS("Outros");

    private String descricao;

    TipoCusto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoCusto deTexto(String texto) {
        if (texto == null) return null;

        String tipo = texto.toUpperCase();

        if (tipo.equalsIgnoreCase("manutenção") || tipo.equalsIgnoreCase("combustível")) {
            if (tipo.equalsIgnoreCase("manutenção")) {
                return MANUTENCAO;
            } else if (tipo.equalsIgnoreCase("combustível")) {
                return COMBUSTIVEL;
            }
        }

        return TipoCusto.valueOf(tipo);
    }
}
