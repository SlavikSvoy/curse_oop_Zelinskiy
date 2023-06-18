package com.example.exampleoop.zombieDialogs;

import com.example.exampleoop.services.ZombieObjectManager;
import com.example.exampleoop.utils.DialogUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.IntStream;

public class ZombieSelectionDlg {
    public void display() {
        Stage window = DialogUtils.createZombieWindow("Вибір зомбі");

        ComboBox<String> cBox = new ComboBox<>();
        List<String> zombies = ZombieObjectManager.getNames();

        IntStream.range(0, zombies.size())
                .mapToObj(i -> (i + 1) + " " + zombies.get(i))
                .forEach(cBox.getItems()::add);

        Button okButton = new Button("OK");
        okButton.setOnAction(actionEvent -> {
            if (cBox.getValue() != null) {
                String[] strChoice = cBox.getValue().split(" ");
                new ZombieChangeDataDlg().display(Integer.parseInt(strChoice[0]) - 1);
            }
            window.close();
        });

        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(cBox, okButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), new Insets(-5))));

        StackPane layout = new StackPane();
        layout.setPadding(new Insets(20));
        layout.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        layout.getChildren().addAll(hbox);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.INSERT)) window.close();
        });

        window.setScene(scene);
        window.setWidth(550);
        window.setHeight(100);
        window.showAndWait();
    }
}