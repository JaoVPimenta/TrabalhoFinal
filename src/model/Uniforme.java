package model;

/**
 * Entidade que representa uma variação de uniforme, definida pela combinação
 * de peça, tamanho, cor e material.
 * <p>
 * Corresponde à tabela {@code uniforme} do banco de dados.
 * </p>
 */
public class Uniforme {
	
	/** Identificador único do uniforme (0 quando ainda não persistido). */
	private int id;

	/** Tipo da peça (ex.: Camisa, Calção, Calça). */
	private String peca;

	/** Tamanho da peça (P, M, G, GG ou XG). */
	private String tamanho;

	/** Cor da peça. */
	private String cor;

	/** Material de confecção (Algodão ou Poliéster). */
	private String material;

	/** Preço base do uniforme, em reais. */
	private double precoBase;
	
	/**
	 * Construtor vazio. Utilizado pelos DAOs ao montar o objeto a partir do banco.
	 */
	public Uniforme() {
		
	}
	
	/**
	 * Construtor completo.
	 *
	 * @param id        identificador único (use 0 para um novo uniforme)
	 * @param peca      tipo da peça
	 * @param tamanho   tamanho da peça
	 * @param cor       cor da peça
	 * @param material  material de confecção
	 * @param precoBase preço base em reais
	 */
	public Uniforme(int id, String peca, String tamanho, String cor, String material, double precoBase) {
		this.id = id;
		this.peca = peca;
		this.tamanho = tamanho;
		this.cor = cor;
		this.material = material;
		this.precoBase = precoBase;
	}
	
	/**
	 * Calcula o preço sugerido de um uniforme a partir do material e do tamanho.
	 * <p>
	 * Regras:
	 * </p>
	 * <ul>
	 *   <li>Preço inicial: R$ 20,00 (material não reconhecido ou nulo);</li>
	 *   <li>Algodão: R$ 35,00;</li>
	 *   <li>Poliéster: R$ 25,00;</li>
	 *   <li>Tamanhos GG e XG: acréscimo de R$ 5,00.</li>
	 * </ul>
	 * A comparação ignora diferenças entre maiúsculas e minúsculas.
	 *
	 * @param material material do uniforme (pode ser {@code null})
	 * @param tamanho  tamanho do uniforme (pode ser {@code null})
	 * @return preço sugerido, em reais
	 */
	public static double calcularPrecoSugerido(String material, String tamanho) {
        double preco = 20.0;
        
        if (material != null) {
            if (material.equalsIgnoreCase("Algodão")) {
                preco = 35.0;
            } else if (material.equalsIgnoreCase("Poliéster")) {
                preco = 25.0;
            }
        }
        if (tamanho != null && (tamanho.equalsIgnoreCase("GG") || tamanho.equalsIgnoreCase("XG"))) {
            preco += 5.0;
        }

        return preco;
    }
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getPeca() {
		return peca;
	}
	
	public void setPeca(String peca) {
		this.peca = peca;
	}
	
	public String getTamanho() {
		return tamanho;
	}
	
	public void setTamanho(String tamanho) {
		this.tamanho = tamanho;
	}
	
	public String getCor() {
		return cor;
	}
	
	public void setCor(String cor) {
		this.cor = cor;
	}
	
	public String getMaterial() {
		return material;
	}
	
	public void setMaterial(String material) {
		this.material = material;
	}
	
	public double getPrecoBase() {
		return precoBase;
	}
	
	public void setPrecoBase(double precoBase) {
		this.precoBase = precoBase;
	}
	
	/**
	 * Representação textual do uniforme, no formato
	 * {@code "PEÇA: <peca> | TAMANHO: <tamanho> | COR: <cor>"}.
	 * <p>
	 * Também é o texto exibido nos {@code ComboBox} que listam uniformes e na
	 * mensagem de confirmação de exclusão.
	 * </p>
	 *
	 * @return texto com peça, tamanho e cor do uniforme
	 */
	@Override
	public String toString() {
		return String.format("PEÇA: %s | TAMANHO: %s | COR: %s", peca, tamanho, cor);
	}
	
}