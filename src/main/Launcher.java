package main;

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author guru
 */
public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent parent = FXMLLoader.load(getClass().getResource("/main/view/main.fxml"));

        primaryStage.setTitle("City Lodge JavaFx Application");
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();

        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            System.out.println("Window Closed: " + event.toString());
        });

    }

    private boolean checkFile(String fileName) {
        File file = new File(fileName);

        return file.exists();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
