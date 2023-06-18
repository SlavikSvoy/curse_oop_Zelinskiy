package com.example.exampleoop.utils;

import com.example.exampleoop.gameEntities.UniversalObject;
import com.example.exampleoop.gameEntities.ZombieLevel;
import com.example.exampleoop.gameEntities.macroObjects.Base;
import com.example.exampleoop.gameEntities.microObjects.RunningZombie;
import com.example.exampleoop.gameEntities.microObjects.WalkingZombie;
import com.example.exampleoop.gameEntities.microObjects.RottingZombie;
import com.example.exampleoop.services.ZombieObjectManager;
import com.google.gson.*;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class SerializationUtils {
    public static void serialize() throws FileNotFoundException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        File file = fileChooser.showSaveDialog(null);

        Map<String, Object> data = new HashMap<>();
        data.put("zombies", ZombieObjectManager.getZombies());
        data.put("base", Base.getInstance());

        try (FileWriter writer = new FileWriter(file, false)) {
            gson.toJson(data, writer);
            DialogUtils.showAlert("Ви успішно виконали серіалізацію!", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            DialogUtils.showAlert("Щось пішло не так!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public static void deserialize() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(null);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonStringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonStringBuilder.append(line);
            }
            String json = jsonStringBuilder.toString();
            JsonObject jsonZombieObject = JsonParser.parseString(json).getAsJsonObject();
            JsonArray jsonZombieArray = jsonZombieObject.getAsJsonArray("zombies");

            ZombieObjectManager.removeAllZombies();
            for (int i = 0; i < jsonZombieArray.size(); i++) {
                JsonObject zombieObject = jsonZombieArray.get(i).getAsJsonObject();
                ZombieLevel zombieLevel = ZombieLevel.valueOf(zombieObject.get("zombieLevel").toString().replace("\"", ""));
                String name = zombieObject.get("name").toString().replace("\"", "");
                int health = zombieObject.get("health").getAsInt();
                double speed = zombieObject.get("speed").getAsDouble();
                double x = zombieObject.get("x").getAsDouble();
                double y = zombieObject.get("y").getAsDouble();
                int brainCount = zombieObject.get("brainCount").getAsInt();
                int mouseCount = zombieObject.get("mouseCount").getAsInt();
                int logCount = zombieObject.get("logCount").getAsInt();

                JsonArray inventoryArray = zombieObject.getAsJsonArray("inventory");
                String[] inventory = new String[inventoryArray.size()];
                IntStream.range(0, inventoryArray.size()).forEach(j -> inventory[j] = inventoryArray.get(j).getAsString());

                boolean isActive = zombieObject.get("isActive").getAsBoolean();

                if (zombieLevel.equals(ZombieLevel.WALKING)) {
                    WalkingZombie walkingZombie = new WalkingZombie(name, health, speed, x, y, inventory, isActive);
                    walkingZombie.setBrainCount(brainCount);
                    walkingZombie.setMouseCount(mouseCount);
                    walkingZombie.setLogCount(logCount);
                    ZombieObjectManager.getZombies().add(walkingZombie);
                } else if (zombieLevel.equals(ZombieLevel.RUNNING)) {
                    RunningZombie runningZombie = new RunningZombie(name, health, speed, x, y, inventory, isActive);
                    runningZombie.setBrainCount(brainCount);
                    runningZombie.setMouseCount(mouseCount);
                    runningZombie.setLogCount(logCount);
                    ZombieObjectManager.getZombies().add(runningZombie);
                } else {
                    RottingZombie rottingZombie = new RottingZombie(name, health, speed, x, y, inventory, isActive);
                    rottingZombie.setBrainCount(brainCount);
                    rottingZombie.setMouseCount(mouseCount);
                    rottingZombie.setLogCount(logCount);
                    ZombieObjectManager.getZombies().add(rottingZombie);
                }
            }
            UniversalObject.updateCountActiveMicroObjects();

            JsonObject jsonBaseObject = JsonParser.parseString(json).getAsJsonObject();
            int brainCount = jsonBaseObject.getAsJsonObject("base").get("brainCount").getAsInt();
            int logCount = jsonBaseObject.getAsJsonObject("base").get("logCount").getAsInt();
            int mouseCount = jsonBaseObject.getAsJsonObject("base").get("mouseCount").getAsInt();
            Base.getInstance().setLogCount(logCount);
            Base.getInstance().setMouseCount(mouseCount);
            Base.getInstance().setBrainCount(brainCount);

            DialogUtils.showAlert("Ви успішно виконали де-серіалізацію!", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            DialogUtils.showAlert("Щось пішло не так!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
}
