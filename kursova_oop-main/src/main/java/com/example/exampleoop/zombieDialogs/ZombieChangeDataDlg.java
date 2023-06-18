package com.example.exampleoop.zombieDialogs;

import com.example.exampleoop.gameEntities.microObjects.WalkingZombie;
import com.example.exampleoop.services.ZombieObjectManager;
import com.example.exampleoop.utils.DialogUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZombieChangeDataDlg {
    public void display(int zombieIndex) {
        Stage window = DialogUtils.createZombieWindow("Введіть нові параметри зомбі!");

        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        List<String> paramsToChange = getZombieParameters(zombieIndex);

        Label nameLabel = new Label("Ім'я:");
        TextField nameText = new TextField(paramsToChange.get(0));
        layout.add(nameLabel, 0, 0);
        layout.add(nameText, 1, 0);

        Label healthLabel = new Label("Здоров'я:");
        TextField healthText = new TextField(paramsToChange.get(1));
        layout.add(healthLabel, 0, 1);
        layout.add(healthText, 1, 1);

        Label speedLabel = new Label("Швидкість:");
        TextField speedText = new TextField(paramsToChange.get(2));
        layout.add(speedLabel, 0, 2);
        layout.add(speedText, 1, 2);

        Label zombieInventoryLabel = new Label("Інвентар зомбі:");
        ComboBox<String> zombieInventoryComboBox = new ComboBox<>();
        zombieInventoryComboBox.getItems().addAll("Палка", "Камінець", "Ніж");
        zombieInventoryComboBox.setValue(paramsToChange.get(3));
        layout.add(zombieInventoryLabel, 0, 3);
        layout.add(zombieInventoryComboBox, 1, 3);

        Label xLabel = new Label("X:");
        TextField xText = new TextField(paramsToChange.get(4));
        layout.add(xLabel, 0, 4);
        layout.add(xText, 1, 4);

        Label yLabel = new Label("Y:");
        TextField yText = new TextField(paramsToChange.get(5));
        layout.add(yLabel, 0, 5);
        layout.add(yText, 1, 5);

        Button okButton = new Button("OK");
        layout.add(okButton, 1, 6);
        okButton.setAlignment(Pos.CENTER);

        okButton.setOnAction(actionEvent -> {
            String name = nameText.getText().trim();
            String health = healthText.getText().trim();
            String speed = speedText.getText().trim();
            String inventory = zombieInventoryComboBox.getValue().trim();
            String x = xText.getText().trim();
            String y = yText.getText().trim();

            try {
                int healthValue = Integer.parseInt(health);
                double speedValue = Double.parseDouble(speed);
                double xValue = Double.parseDouble(x);
                double yValue = Double.parseDouble(y);

                if (healthValue > 100 || speedValue > 10) {
                    DialogUtils.showAlert("Значення здоров'я та швидкості не можуть бути більшими за 100 та 10 відповідно!",
                            Alert.AlertType.ERROR);
                    return;
                }

                updateZombieData(zombieIndex, name, healthValue, speedValue, xValue, yValue, new String[]{inventory});
                DialogUtils.showAlert("Ви успішно змінили дані мікрооб'єкту", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                e.printStackTrace();
                DialogUtils.showAlert("Помилка, щось пішло не так!", Alert.AlertType.ERROR);
            }
            window.close();
        });
        window.setScene(new Scene(layout, 270, 310));
        window.showAndWait();
    }

    private List<String> getZombieParameters(int index) {
        List<String> paramsToChange = new ArrayList<>();
        WalkingZombie walkingZombie = ZombieObjectManager.getZombies().get(index);
        getParams(paramsToChange, walkingZombie.getName(), walkingZombie.getHealth(), walkingZombie.getSpeed(), walkingZombie.getX(), walkingZombie.getY(), walkingZombie.getInventory());
        return paramsToChange;
    }

    private void getParams(List<String> paramsToChange, String name, int health, double speed, double x, double y, String[] inventory) {
        paramsToChange.add(name);
        paramsToChange.add(String.valueOf(health));
        paramsToChange.add(String.valueOf(speed));
        paramsToChange.add(Arrays.toString(inventory));
        paramsToChange.add(String.valueOf(x));
        paramsToChange.add(String.valueOf(y));
    }

    private void updateZombieData(int index, String name, int health, double speed, double x, double y, String[] inventory) {
        WalkingZombie zombie = ZombieObjectManager.getZombies().get(index);
        if (zombie != null) updateZombieData(zombie, name, health, speed, x, y, inventory);
    }

    private void updateZombieData(WalkingZombie walkingZombie, String name, int health, double speed, double x, double y, String[] inventory) {
        walkingZombie.setName(name);
        walkingZombie.setHealth(health);
        walkingZombie.setSpeed(speed);
        walkingZombie.setInventory(inventory);
        walkingZombie.setPosition(x, y);
    }
}
