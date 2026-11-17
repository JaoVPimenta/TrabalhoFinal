package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application {

    @Override
    public void start(Stage palco) throws Exception {
       
        Parent raiz = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));

        palco.setTitle("SGU - Sistema de Gestão de Uniformes");
        palco.setScene(new Scene(raiz));
        palco.setResizable(false);
        palco.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}