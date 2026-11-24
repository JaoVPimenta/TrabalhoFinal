package service;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import dao.Conexao;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class RelatorioService {

    private static final String PASTA_RELATORIOS = "/relatorios/";

    public void gerarCatalogoUniformes() {
        gerar(PASTA_RELATORIOS + "Catalogo.jrxml", new HashMap<>(), "Catálogo de Uniformes - SGU");
    }

    public void gerarProdutividadeMensal(int mes, int ano) {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("MES", mes);
        parametros.put("ANO", ano);

        String titulo = String.format("Produtividade Mensal (%02d/%d) - SGU", mes, ano);
        gerar(PASTA_RELATORIOS + "ProdutividadeMensal.jrxml", parametros, titulo);
    }

    private void gerar(String caminhoJrxml, Map<String, Object> parametros, String tituloJanela) {
        try (Connection conn = Conexao.getConnection()) {
            InputStream jrxmlStream = getClass().getResourceAsStream(caminhoJrxml);
            if (jrxmlStream == null) {
                throw new IllegalStateException("Arquivo não encontrado no classpath: " + caminhoJrxml);
            }

            JasperReport report = JasperCompileManager.compileReport(jrxmlStream);
            JasperPrint print = JasperFillManager.fillReport(report, parametros, conn);

            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setTitle(tituloJanela);
            viewer.setLocationRelativeTo(null);
            viewer.setVisible(true);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório (" + tituloJanela + "): " + e.getMessage(), e);
        }
    }
}