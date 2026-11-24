package model;

/**
 * Tipos de peça que podem compor um {@link Uniforme}.
 * <p>
 * O nome da constante (ex.: {@code CAMISETA}) é o valor gravado na coluna
 * {@code peca} da tabela {@code uniforme}.
 * </p>
 */
public enum Peca {

    /** Camiseta. */
    CAMISETA,

    /** Calça. */
    CALCA,

    /** Boné. */
    BONE
}