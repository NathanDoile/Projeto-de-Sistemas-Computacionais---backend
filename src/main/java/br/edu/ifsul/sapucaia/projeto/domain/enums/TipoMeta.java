package br.edu.ifsul.sapucaia.projeto.domain.enums;

public enum TipoMeta {
    RECEITA,
    CUSTO;

    public static TipoMeta deTexto(String texto){
        if(texto == null) return null;

        String t = texto.toUpperCase();

        if(t.equalsIgnoreCase("ganho") ||
                t.equalsIgnoreCase("receita")){
            return RECEITA;
        }
        else if(t.equalsIgnoreCase("gasto") ||
                t.equalsIgnoreCase("custo") ||
                t.equalsIgnoreCase("limite de gasto")){
            return CUSTO;
        }
        else{
            return TipoMeta.valueOf(t);
        }
    }
}