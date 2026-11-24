package model;

/**
 * Tipos de peça que podem compor um {@link Uniforme}.
 * <p>
 * O nome da constante (ex.: {@code CAMISETA}) é o valor gravado na coluna
 * {@code peca} da tabela {@code uniforme}. Já o {@link #toString()} devolve o
 * texto amigável ("Camiseta"), usado nos ComboBoxes e nas tabelas.
 * </p>
 */
public enum Peca {

    /** Camiseta. */
    CAMISETA("Camiseta"),

    /** Calça. */
    CALCA("Calça"),

    /** Boné. */
    BONE("Boné");

    /** Texto exibido para o usuário. */
    private final String descricao;

    Peca(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Texto exibido nas telas. Não afeta o que é gravado no banco
     * (o DAO usa {@code name()}).
     *
     * @return nome da peça formatado para exibição
     */
    @Override
    public String toString() {
        return descricao;
    }
}
