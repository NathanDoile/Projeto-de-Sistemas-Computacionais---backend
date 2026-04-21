package br.edu.ifsul.sapucaia.projeto.domain.enums;

public enum TipoManutencao {
    PREVENTIVA("Preventiva"),
    CORRETIVA("Corretiva"),
    PREDITIVA("Preditiva");

    private String descricao;

    TipoManutencao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoManutencao deTexto(String texto) {
        if (texto == null) return null;

        String tipo = texto.toUpperCase();

        return TipoManutencao.valueOf(tipo);
    }
}
