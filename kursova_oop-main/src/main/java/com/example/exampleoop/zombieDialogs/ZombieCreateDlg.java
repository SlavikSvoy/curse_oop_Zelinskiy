package com.example.exampleoop.zombieDialogs;

import com.example.exampleoop.gameEntities.ZombieLevel;
import com.example.exampleoop.gameEntities.microObjects.RunningZombie;
import com.example.exampleoop.gameEntities.microObjects.WalkingZombie;
import com.example.exampleoop.gameEntities.microObjects.RottingZombie;
import com.example.exampleoop.services.ZombieObjectManager;
import com.example.exampleoop.utils.DialogUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ZombieCreateDlg {
    public void display(double x, double y) {
        Stage window = DialogUtils.createZombieWindow("Створення нового зомбі!");

        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        layout.setHgap(11);
        layout.setVgap(11);
        layout.setPadding(new Insets(20, 20, 20, 20));

        Label nameLabel = new Label("Ім'я:");
        TextField nameText = new TextField();
        layout.add(nameLabel, 0, 0);
        layout.add(nameText, 1, 0);

        Label healthLabel = new Label("Здоров'я:");
        TextField healthText = new TextField();
        layout.add(healthLabel, 0, 1);
        layout.add(healthText, 1, 1);

        Label speedLabel = new Label("Швидкість:");
        TextField speedText = new TextField();
        layout.add(speedLabel, 0, 2);
        layout.add(speedText, 1, 2);

        Label zombieTypeLabel = new Label("Тип зомбі:");
        ComboBox<String> zombieTypeComboBox = new ComboBox<>();
        zombieTypeComboBox.getItems().addAll("WALKING", "RUNNING", "ROTTING");
        zombieTypeComboBox.setValue("WALKING");
        layout.add(zombieTypeLabel, 0, 3);
        layout.add(zombieTypeComboBox, 1, 3);

        Label zombieInventoryLabel = new Label("Інвентар зомбі:");
        ComboBox<String> zombieInventoryComboBox = new ComboBox<>();
        zombieInventoryComboBox.getItems().addAll("Палка", "Камінець", "Ніж");
        zombieInventoryComboBox.setValue("Камінець");
        layout.add(zombieInventoryLabel, 0, 4);
        layout.add(zombieInventoryComboBox, 1, 4);

        Label xLabel = new Label("X:");
        TextField xText = new TextField();
        xText.setText(Double.toString(x));
        layout.add(xLabel, 0, 5);
        layout.add(xText, 1, 5);

        Label yLabel = new Label("Y:");
        TextField yText = new TextField();
        yText.setText(Double.toString(y));
        layout.add(yLabel, 0, 6);
        layout.add(yText, 1, 6);

        CheckBox activeCheckBox = new CheckBox("Активувати об'єкт");
        activeCheckBox.setSelected(false);
        layout.add(activeCheckBox, 0, 8, 3, 1);
        activeCheckBox.setAlignment(Pos.CENTER);

        Button okButton = new Button("OK");
        layout.add(okButton, 1, 9);
        okButton.setAlignment(Pos.CENTER);

        okButton.setOnAction(event -> {
            String name = nameText.getText().trim();
            String health = healthText.getText().trim();
            String speed = speedText.getText().trim();
            String inventory = zombieInventoryComboBox.getValue().trim();
            ZombieLevel zombieLevel = ZombieLevel.valueOf(zombieTypeComboBox.getValue().trim());

            try {
                if (name.isEmpty() && health.isEmpty() && speed.isEmpty()) {
                    if (zombieLevel.equals(ZombieLevel.WALKING)) {
                        ZombieObjectManager.addZombie(new WalkingZombie());
                    } else if (zombieLevel.equals(ZombieLevel.RUNNING)) {
                        ZombieObjectManager.addZombie(new RunningZombie());
                    } else {
                        ZombieObjectManager.addZombie(new RottingZombie());
                    }
                    DialogUtils.showAlert("Ви успішно створили новий мікрооб'єкт", Alert.AlertType.INFORMATION);
                    window.close();
                    return;
                }

                int healthValue = Integer.parseInt(health);
                double speedValue = Double.parseDouble(speed);
                boolean isActive = activeCheckBox.isSelected();

                if (healthValue > 100 || speedValue > 10) {
                    DialogUtils.showAlert(
                            "Значення здоров'я та швидкості не можуть бути більшими за 100 та 10 відповідно!",
                            Alert.AlertType.ERROR);
                    return;
                }
                if (zombieLevel.equals(ZombieLevel.WALKING)) {
                    ZombieObjectManager.addZombie(new WalkingZombie(name, healthValue, speedValue, x, y, new String[]{inventory}, isActive));
                } else if (zombieLevel.equals(ZombieLevel.RUNNING)) {
                    ZombieObjectManager.addZombie(new RunningZombie(name, healthValue, speedValue, x, y, new String[]{inventory}, isActive));
                } else {
                    ZombieObjectManager.addZombie(new RottingZombie(name, healthValue, speedValue, x, y, new String[]{inventory}, isActive));
                }
                DialogUtils.showAlert("Ви успішно створили новий мікрооб'єкт", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                e.printStackTrace();
                DialogUtils.showAlert("Помилка, щось пішло не так!", Alert.AlertType.ERROR);
            }
            window.close();
        });

        window.setScene(new Scene(layout, 280, 380));
        window.showAndWait();
    }
}