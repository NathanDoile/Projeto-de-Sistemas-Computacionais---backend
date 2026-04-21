package br.edu.ifsul.sapucaia.projeto.domain.enums;

public enum TipoVeiculo {
    CARRO,
    MOTOCICLETA;

    public static TipoVeiculo deTexto(String texto){
        if (texto == null) return null;

        String t = texto.toUpperCase();

        try {
            return TipoVeiculo.valueOf(t);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de veículo inválido: " + texto);
        }
    }
}
