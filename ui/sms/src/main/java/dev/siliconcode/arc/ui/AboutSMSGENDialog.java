package dev.siliconcode.arc.ui;

import javafx.fxml.FXML;
import javafx.scene.image.Image;

public class AboutSMSGENDialog extends AboutDialog {

    public AboutSMSGENDialog() {

    }

    @FXML
    @Override
    public void initialize() {
        Image image = new Image(getClass().getResourceAsStream("/sample/sms_gen_full_invert.png"));
        imgLogo.setImage(image);

        lblCopyRight.setText("Copyright (C) Idaho State University Empirical Software Engineering Laboratory.");

        lblTagLine.setText("SMS*GEN - A Tool to Improve the SMS Process");

        lblBody.setText("Author: Isaac Griffith");
    }
}
