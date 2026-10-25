package model;

import java.time.LocalDate;

public class RegistroProducao {
    private int id;
    private Funcionario funcionario;
    private Uniforme uniforme;
    private int quantidade;
    private LocalDate dataProducao;

    public RegistroProducao() {
    }

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
