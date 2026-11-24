package model;

/**
 * Representa um uniforme produzido pela empresa.
 * <p>
 * Cada uniforme é descrito por peça, tamanho, cor, material e preço base.
 * Peça, tamanho e material são enums ({@link Peca}, {@link Tamanho} e
 * {@link Material}); a cor é um texto livre. A classe também concentra a regra
 * de negócio que sugere o preço do uniforme.
 * </p>
 * <p>
 * Um uniforme pode aparecer em vários {@link RegistroProducao}.
 * </p>
 */
public class Uniforme {

	private int id;
	private Peca peca;
	private Tamanho tamanho;
	private String cor;
	private Material material;
	private double precoBase;

	/**
	 * Construtor padrão. Cria um uniforme sem dados preenchidos,
	 * usado pelos DAOs ao montar objetos a partir do banco.
	 */
	public Uniforme() {

	}

	/**
	 * Cria um uniforme com todos os atributos informados.
	 *
	 * @param id        identificador do uniforme (0 quando ainda não foi gravado no banco)
	 * @param peca      tipo da peça
	 * @param tamanho   tamanho da peça
	 * @param cor       cor do uniforme
	 * @param material  material de fabricação
	 * @param precoBase preço base do uniforme, em reais
	 */
	public Uniforme(int id, Peca peca, Tamanho tamanho, String cor, Material material, double precoBase) {
		this.id = id;
		this.peca = peca;
		this.tamanho = tamanho;
		this.cor = cor;
		this.material = material;
		this.precoBase = precoBase;
	}

	/**
	 * Calcula o preço sugerido de um uniforme conforme o material e o tamanho.
	 * <p>
	 * Regra de negócio:
	 * </p>
	 * <ul>
	 *   <li>preço inicial de R$ 20,00 (quando o material não é informado);</li>
	 *   <li>{@link Material#ALGODAO}: R$ 35,00;</li>
	 *   <li>{@link Material#POLIESTER}: R$ 25,00;</li>
	 *   <li>tamanho {@link Tamanho#GG}: acréscimo de R$ 5,00.</li>
	 * </ul>
	 *
	 * @param material material de fabricação do uniforme (pode ser {@code null})
	 * @param tamanho  tamanho do uniforme (pode ser {@code null})
	 * @return preço sugerido, em reais
	 */
	public static double calcularPrecoSugerido(Material material, Tamanho tamanho) {
        double preco = 20.0;

        if (material == Material.ALGODAO) {
            preco = 35.0;
        } else if (material == Material.POLIESTER) {
            preco = 25.0;
        }

        if (tamanho == Tamanho.GG) {
            preco += 5.0;
        }

        return preco;
    }

	/**
	 * Retorna o identificador do uniforme.
	 *
	 * @return id do uniforme
	 */
	public int getId() {
		return id;
	}

	/**
	 * Define o identificador do uniforme.
	 *
	 * @param id novo id do uniforme
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Retorna o tipo da peça.
	 *
	 * @return peça do uniforme
	 */
	public Peca getPeca() {
		return peca;
	}

	/**
	 * Define o tipo da peça.
	 *
	 * @param peca nova peça do uniforme
	 */
	public void setPeca(Peca peca) {
		this.peca = peca;
	}

	/**
	 * Retorna o tamanho do uniforme.
	 *
	 * @return tamanho do uniforme
	 */
	public Tamanho getTamanho() {
		return tamanho;
	}

	/**
	 * Define o tamanho do uniforme.
	 *
	 * @param tamanho novo tamanho do uniforme
	 */
	public void setTamanho(Tamanho tamanho) {
		this.tamanho = tamanho;
	}

	/**
	 * Retorna a cor do uniforme.
	 *
	 * @return cor do uniforme
	 */
	public String getCor() {
		return cor;
	}

	/**
	 * Define a cor do uniforme.
	 *
	 * @param cor nova cor do uniforme
	 */
	public void setCor(String cor) {
		this.cor = cor;
	}

	/**
	 * Retorna o material de fabricação.
	 *
	 * @return material do uniforme
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * Define o material de fabricação.
	 *
	 * @param material novo material do uniforme
	 */
	public void setMaterial(Material material) {
		this.material = material;
	}

	/**
	 * Retorna o preço base do uniforme.
	 *
	 * @return preço base, em reais
	 */
	public double getPrecoBase() {
		return precoBase;
	}

	/**
	 * Define o preço base do uniforme.
	 *
	 * @param precoBase novo preço base, em reais
	 */
	public void setPrecoBase(double precoBase) {
		this.precoBase = precoBase;
	}

	/**
	 * Retorna uma descrição resumida do uniforme (peça, tamanho e cor).
	 * <p>
	 * Esse texto é exibido nos ComboBoxes e nas mensagens de confirmação das telas.
	 * </p>
	 *
	 * @return texto no formato {@code PEÇA: ... | TAMANHO: ... | COR: ...}
	 */
	@Override
	public String toString() {
		return String.format("PEÇA: %s | TAMANHO: %s | COR: %s", peca, tamanho, cor);
	}

}