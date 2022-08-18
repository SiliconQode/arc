package dev.siliconcode.arc.ui;

import javafx.fxml.FXML;
import javafx.scene.image.Image;

public class AboutSMSMDCDialog extends AboutDialog {

    public AboutSMSMDCDialog() {

    }

    @FXML
    @Override
    public void initialize() {
        Image image = new Image(getClass().getResourceAsStream("/sample/sms_mdc_full_invert.png"));
        imgLogo.setImage(image);

        lblCopyRight.setText("Copyright (C) Idaho State University Empirical Software Engineering Laboratory.");

        lblTagLine.setText("SMS*MDC - A Tool to help Evaluate SMS*GEN");

        lblBody.setText("Author: Isaac Griffith");
    }
}
