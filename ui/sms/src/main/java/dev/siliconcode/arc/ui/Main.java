package dev.siliconcode.arc.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();

        // Create the FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new AboutSMSGENDialog());
        // Path to the FXML File
        String fxmlDocPath = "/sample/AboutDialog.fxml";

        // Create the Pane and all Details
        Parent root = loader.load(getClass().getResourceAsStream(fxmlDocPath));

        // Create the Scene
        Scene scene = new Scene(root);
        // Set the Scene to the Stage
        primaryStage.setScene(scene);
        // Set the Title to the Stage
        primaryStage.setTitle("About Dialog");
        // Display the Stage
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
