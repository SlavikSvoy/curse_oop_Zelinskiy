package com.example.exampleoop.zombieDialogs;

import com.example.exampleoop.gameEntities.microObjects.WalkingZombie;
import com.example.exampleoop.services.ZombieObjectManager;
import com.example.exampleoop.utils.DialogUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.function.Predicate;

public class ZombieSearchDlg {
    public void display() {
        Stage window = new Stage();
        window.setTitle("Пошук зомбі за параметрами");

        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        layout.setHgap(2);
        layout.setVgap(2);
        layout.setPadding(new Insets(20, 20, 20, 20));

        Label nameLabel = new Label("Ім'я:");
        TextField nameText = new TextField();
        layout.add(nameLabel, 0, 0);
        layout.add(nameText, 1, 0);

        Label speedLabel = new Label("Швидкість:");
        TextField speedText = new TextField();
        layout.add(speedLabel, 0, 1);
        layout.add(speedText, 1, 1);

        Button okButton = new Button("OK");
        layout.add(okButton, 0, 2, 2, 1);
        okButton.setAlignment(Pos.CENTER);

        // Додати стилі для кнопки, щоб вона була посередині
        okButton.setStyle("-fx-translate-x: 50%");

        okButton.setOnAction(actionEvent -> {
            String zombieName = nameText.getText().trim();
            String zombieSpeed = speedText.getText().trim();

            if (zombieName.isEmpty() && zombieSpeed.isEmpty()) {
                DialogUtils.showAlert("Поля не заповнені!", Alert.AlertType.ERROR);
                return;
            }

            Predicate<WalkingZombie> filterPredicate = zombie -> true;

            if (!zombieName.isEmpty()) {
                filterPredicate = filterPredicate.and(zombie -> zombieName.equalsIgnoreCase(zombie.getName()));
            }

            if (!zombieSpeed.isEmpty()) {
                try {
                    double speedValue = Double.parseDouble(zombieSpeed);
                    filterPredicate = filterPredicate.and(zombie -> speedValue == zombie.getSpeed());
                } catch (NumberFormatException e) {
                    DialogUtils.showAlert("Невірне значення швидкості!", Alert.AlertType.ERROR);
                    return;
                }
            }

            ZombieObjectManager.filterZombies(filterPredicate);

            window.close();
        });

        window.setScene(new Scene(layout, 300, 250));
        window.showAndWait();
    }
}
