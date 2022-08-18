package dev.siliconcode.arc.ui;

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.javafx.IconFontFX;
import jiconfont.javafx.IconNode;

public class EditableListPane<T> {

//    private <?> editEventHandler;
//    private EventHandler<?> addEventHandler;
    private boolean showEdit;
    private boolean showUpDown;
    private String text;

    public void init() {
        IconFontFX.register(FontAwesome.getIconFont());

        BorderPane pane = new BorderPane();
        Scene scene = new Scene(pane);

        Label label = new Label();
        label.setText(text);
        pane.setTop(label);

        ScrollPane spane = new ScrollPane();
        spane.setFitToHeight(true);
        spane.setFitToWidth(true);

        ListView<T> list = new ListView<>();
        spane.setContent(list);

        list.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateButtons();
            }
        });

        pane.setCenter(spane);

        FlowPane flow = new FlowPane();
        flow.setColumnHalignment(HPos.CENTER);
        flow.setRowValignment(VPos.TOP);
        flow.setHgap(5);
        flow.setVgap(5);
        flow.setOrientation(Orientation.VERTICAL);

        IconNode inEdit = new IconNode(FontAwesome.PENCIL_SQUARE);
        inEdit.setIconSize(40);
        inEdit.setFill(Color.BLACK);

        IconNode inAdd = new IconNode(FontAwesome.PLUS_SQUARE);
        inAdd.setIconSize(40);
        inAdd.setFill(Color.BLACK);

        IconNode inRemove = new IconNode(FontAwesome.MINUS_SQUARE);
        inRemove.setIconSize(40);
        inRemove.setFill(Color.BLACK);

        IconNode inUp = new IconNode(FontAwesome.CARET_SQUARE_O_UP);
        inUp.setIconSize(40);
        inUp.setFill(Color.BLACK);

        IconNode inDown = new IconNode(FontAwesome.CARET_SQUARE_O_DOWN);
        inDown.setIconSize(40);
        inDown.setFill(Color.BLACK);

        Label lblAdd = new Label();
        lblAdd.setGraphic(inAdd);

        Label lblRemove = new Label();
        lblRemove.setGraphic(inRemove);

        Label lblEdit = new Label();
        lblEdit.setGraphic(inEdit);

        Label lblUp = new Label();
        lblUp.setGraphic(inUp);

        Label lblDown = new Label();
        lblDown.setGraphic(inDown);

        flow.getChildren().add(lblAdd);
        flow.getChildren().add(lblRemove);
        flow.getChildren().add(lblEdit);
        flow.getChildren().add(lblUp);
        flow.getChildren().add(lblDown);

        pane.setRight(flow);
    }

    private void updateButtons() {

    }
}
