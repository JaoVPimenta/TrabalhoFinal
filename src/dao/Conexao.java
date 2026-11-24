package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitária responsável por fornecer conexões com o banco de dados
 * MySQL do sistema ({@code db_sgu}).
 * <p>
 * Todos os DAOs obtêm suas conexões por meio desta classe.
 * </p>
 */
public class Conexao {
	
	//Dados de conexão

	/** URL JDBC do banco de dados {@code db_sgu} (MySQL em localhost, porta 3306). */
	private static final String URL = "jdbc:mysql://localhost:3306/db_sgu?useSSL=false&serverTimezone=UTC";

	/** Usuário utilizado para autenticação no banco de dados. */
	private static final String USUARIO = "root";

	/** Senha utilizada para autenticação no banco de dados. */
	private static final String SENHA = "40028922";
	
	//Método para obter a conexão

	/**
	 * Abre uma nova conexão com o banco de dados.
	 * <p>
	 * Cada chamada cria uma conexão nova, que deve ser fechada por quem a
	 * solicitou (por exemplo, com try-with-resources).
	 * </p>
	 *
	 * @return conexão aberta com o banco de dados
	 * @throws SQLException caso não seja possível conectar (banco indisponível,
	 *                      credenciais inválidas, etc.)
	 */
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USUARIO, SENHA);
	}
	
}