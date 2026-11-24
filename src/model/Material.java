package model;

/**
 * Materiais de fabricação disponíveis para um {@link Uniforme}.
 * <p>
 * O nome da constante (ex.: {@code ALGODAO}) é o valor gravado na coluna
 * {@code material} da tabela {@code uniforme}. Já o {@link #toString()}
 * devolve o texto amigável ("Algodão"), usado nos ComboBoxes e nas tabelas.
 * O material é um dos fatores da regra de preço sugerido
 * ({@link Uniforme#calcularPrecoSugerido(Material, Tamanho)}).
 * </p>
 */
public enum Material {

    /** Algodão. */
    ALGODAO("Algodão"),

    /** Poliéster. */
    POLIESTER("Poliéster");

    /** Texto exibido para o usuário. */
    private final String descricao;

    Material(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Texto exibido nas telas. Não afeta o que é gravado no banco
     * (o DAO usa {@code name()}).
     *
     * @return nome do material formatado para exibição
     */
    @Override
    public String toString() {
        return descricao;
    }
}
