package model;

public class Uniforme {
	
	private int id;
	private String peca;
	private String tamanho;
	private String cor;
	private String material;
	private double precoBase;
	
	public Uniforme() {
		
	}
	
	public Uniforme(int id, String peca, String tamanho, String cor, String material, double precoBase) {
		this.id = id;
		this.peca = peca;
		this.tamanho = tamanho;
		this.cor = cor;
		this.material = material;
		this.precoBase = precoBase;
	}
	
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
	
	@Override
	public String toString() {
		return String.format("PEÇA: %s | TAMANHO: %s | COR: %s", peca, tamanho, cor);
	}
	
}
