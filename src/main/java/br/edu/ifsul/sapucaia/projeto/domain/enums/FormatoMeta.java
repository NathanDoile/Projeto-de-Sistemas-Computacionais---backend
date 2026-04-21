package br.edu.ifsul.sapucaia.projeto.domain.enums;

public enum FormatoMeta {
    DIARIA, MENSAL, SEMANAL;

    public static FormatoMeta deTexto(String texto){
        if (texto == null) return null;

        String t = texto.toUpperCase();

        if(t.equalsIgnoreCase("diária") || t.equalsIgnoreCase("diaria")){
            return DIARIA;
        }
        else{
            return FormatoMeta.valueOf(t);
        }
    }
}