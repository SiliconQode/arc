package dev.siliconcode.arc.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SplashScreen extends Stage {

    String logoResource;

    public SplashScreen() {
    }

    public SplashScreen(StageStyle style) {
        super(style);
    }

    private void init() {
        BorderPane pane = new BorderPane();

        Image img = new Image(getClass().getResourceAsStream(logoResource));
        ImageView logo = new ImageView(img);

    }
}
