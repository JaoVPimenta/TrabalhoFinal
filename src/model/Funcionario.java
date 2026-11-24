package model;

import java.time.LocalDate;

/**
 * Entidade que representa um funcionário da empresa.
 * <p>
 * Corresponde à tabela {@code funcionario} do banco de dados.
 * </p>
 */
public class Funcionario {

	/** Identificador único do funcionário (0 quando ainda não persistido). */
	private int id;

	/** Nome completo do funcionário. */
	private String nome;

	/** CPF do funcionário, armazenado apenas com dígitos (11 caracteres). */
	private String cpf;

	/** Cargo ocupado pelo funcionário. */
	private String cargo;

	/** Data de admissão do funcionário na empresa. */
	private LocalDate dataAdmissao;
	
	/**
	 * Construtor vazio. Utilizado pelos DAOs ao montar o objeto a partir do banco.
	 */
	public Funcionario() {
		
	}
	
	/**
	 * Construtor completo.
	 *
	 * @param id           identificador único (use 0 para um novo funcionário)
	 * @param nome         nome completo
	 * @param cpf          CPF somente com dígitos
	 * @param cargo        cargo ocupado
	 * @param dataAdmissao data de admissão
	 */
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
	
	/**
	 * Representação textual do funcionário, no formato
	 * {@code "NOME: <nome> | CARGO: <cargo>"}.
	 * <p>
	 * Também é o texto exibido nos {@code ComboBox} que listam funcionários.
	 * </p>
	 *
	 * @return texto com nome e cargo do funcionário
	 */
	@Override
	public String toString() {
		return String.format("NOME: %s | CARGO: %s", nome, cargo);
	}
	
	
}