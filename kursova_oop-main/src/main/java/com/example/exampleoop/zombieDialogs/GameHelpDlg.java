package com.example.exampleoop.zombieDialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameHelpDlg {
    public void display() {
        Stage helpStage = new Stage();
        helpStage.setTitle("Команди гри");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);

        Text title = new Text("Команди гри:");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Text commands = new Text("1. ↑ - рух камерою вгору.\n" +
                "2. ↓ - рух камерою вниз.\n" +
                "3. ← - рух камерою вліво.\n" +
                "4. → - рух камерою вправо.\n" +
                "5. W - рух вгору.\n" +
                "6. S - рух вниз.\n" +
                "7. A - рух вправо.\n" +
                "8. D - рух вліво.\n" +
                "9. T - виділення всіх RottingZombie.\n" +
                "10. J - клонування всіх активних мікрооб'єктів.\n" +
                "11. B - відправлення всіх мікрооб'єктів на базу.\n" +
                "12. INSERT - створення нового мікрооб'єкта.\n" +
                "13. DELETE - видалення всіх активних мікрооб'єктів."
        );

        vbox.getChildren().addAll(title, commands);
        vbox.getStyleClass().add("dialog-box");

        Scene helpScene = new Scene(vbox, 320, 400);
        helpStage.setScene(helpScene);
        helpStage.show();
    }
}