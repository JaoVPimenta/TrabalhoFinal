package model;

import java.time.LocalDate;

public class Funcionario {

	private int id;
	private String nome;
	private String cpf;
	private String cargo;
	private LocalDate dataAdmissao;
	
	public Funcionario() {
		
	}
	
	public Funcionario(int id, String nome, String cpf, String cargo, LocalDate dataAdmissao) {
		this.id = id;
		this.nome = nome;
		this.cpf = cpf;
		this.cargo = cargo;
		this.dataAdmissao = dataAdmissao;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getCargo() {
		return cargo;
	}
	
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	
	public LocalDate getDataAdmissao() {
		return dataAdmissao;
	}
	
	public void setDataAdmissao(LocalDate dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}
	
	@Override
	public String toString() {
		return String.format("NOME: %s | CARGO: %s", nome, cargo);
	}
	
	
}
