package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principal do SGU (Sistema de Gestão de Uniformes).
 * <p>
 * Estende {@link Application} e é responsável por inicializar a interface
 * JavaFX, carregando a tela principal ({@code /view/MainView.fxml}).
 * </p>
 */
public class App extends Application {

    /**
     * Ponto de entrada da interface JavaFX.
     * <p>
     * Carrega o FXML da tela principal, define o título da janela, cria a cena,
     * desabilita o redimensionamento e exibe o palco.
     * </p>
     *
     * @param palco janela principal (Stage) fornecida pelo JavaFX
     * @throws Exception caso o arquivo FXML não seja encontrado ou não possa ser carregado
     */
    @Override
    public void start(Stage palco) throws Exception {
       
        Parent raiz = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));

        palco.setTitle("SGU - Sistema de Gestão de Uniformes");
        palco.setScene(new Scene(raiz));
        palco.setResizable(false);
        palco.show();
    }

    /**
     * Método de entrada da JVM. Inicia a aplicação JavaFX.
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        launch(args);
    }
}