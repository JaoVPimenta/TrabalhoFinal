package model;

/**
 * Materiais de fabricação disponíveis para um {@link Uniforme}.
 * <p>
 * O nome da constante (ex.: {@code ALGODAO}) é o valor gravado na coluna
 * {@code material} da tabela {@code uniforme}. O material é um dos fatores
 * da regra de preço sugerido
 * ({@link Uniforme#calcularPrecoSugerido(Material, Tamanho)}).
 * </p>
 */
public enum Material {

    /** Algodão. */
    ALGODAO,

    /** Poliéster. */
    POLIESTER
}