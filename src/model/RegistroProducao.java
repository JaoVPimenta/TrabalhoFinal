package model;

import java.time.LocalDate;

/**
 * Entidade que representa o lançamento de produção de um uniforme por um
 * funcionário em determinada data.
 * <p>
 * Corresponde à tabela {@code registro_producao} do banco de dados, que
 * relaciona {@link Funcionario} e {@link Uniforme}.
 * </p>
 */
public class RegistroProducao {

    /** Identificador único do registro (0 quando ainda não persistido). */
    private int id;

    /** Funcionário responsável pela produção. */
    private Funcionario funcionario;

    /** Uniforme que foi produzido. */
    private Uniforme uniforme;

    /** Quantidade de unidades produzidas. */
    private int quantidade;

    /** Data em que a produção foi realizada. */
    private LocalDate dataProducao;

    /**
     * Construtor vazio. Utilizado pelos DAOs ao montar o objeto a partir do banco.
     */
    public RegistroProducao() {
    }

    /**
     * Construtor completo.
     *
     * @param id           identificador único (use 0 para um novo registro)
     * @param funcionario  funcionário responsável pela produção
     * @param uniforme     uniforme produzido
     * @param quantidade   quantidade de unidades produzidas
     * @param dataProducao data da produção
     */
    public RegistroProducao(int id, Funcionario funcionario, Uniforme uniforme, int quantidade, LocalDate dataProducao) {
        this.id = id;
        this.funcionario = funcionario;
        this.uniforme = uniforme;
        this.quantidade = quantidade;
        this.dataProducao = dataProducao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Uniforme getUniforme() {
        return uniforme;
    }

    public void setUniforme(Uniforme uniforme) {
        this.uniforme = uniforme;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDate getDataProducao() {
        return dataProducao;
    }

    public void setDataProducao(LocalDate dataProducao) {
        this.dataProducao = dataProducao;
    }
}