package model;

/**
 * Tamanhos disponíveis para as peças de {@link Uniforme}.
 * <p>
 * O nome da constante (ex.: {@code GG}) é o valor gravado na coluna
 * {@code tamanho} da tabela {@code uniforme}. O tamanho {@link #GG} possui
 * acréscimo na regra de preço sugerido
 * ({@link Uniforme#calcularPrecoSugerido(Material, Tamanho)}).
 * </p>
 */
public enum Tamanho {

    /** Tamanho pequeno. */
    P,

    /** Tamanho médio. */
    M,

    /** Tamanho grande. */
    G,

    /** Tamanho extra grande. */
    GG
}