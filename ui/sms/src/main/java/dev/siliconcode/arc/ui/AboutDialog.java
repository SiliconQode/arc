package dev.siliconcode.arc.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AboutDialog {

    @FXML
    protected ImageView imgLogo;

    @FXML
    protected Label lblCopyRight;

    @FXML
    protected Label lblTagLine;

    @FXML
    protected Label lblBody;

    @FXML
    protected URL location;

    @FXML
    protected ResourceBundle resources;

    public AboutDialog() {

    }


    public abstract void initialize();
}
