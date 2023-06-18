package com.example.exampleoop.gameEntities;

import com.example.exampleoop.Main;
import com.example.exampleoop.gameEntities.macroObjects.Base;
import com.example.exampleoop.gameEntities.macroObjects.Field;
import com.example.exampleoop.gameEntities.macroObjects.Forest;
import com.example.exampleoop.gameEntities.macroObjects.City;
import com.example.exampleoop.gameEntities.microObjects.WalkingZombie;
import com.example.exampleoop.services.MacroObjectManager;
import com.example.exampleoop.services.ZombieObjectManager;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;

public class UniversalObject {
    private static final int imageWidth = 3000;
    private static final int imageHeight = 2600;
    private static final double mapCoefficientCount = 1.5d;
    private static final Label labelCountActiveMicroObjects = new Label();
    private static final Label labelCountResourcesOnBase = new Label();
    private static UniversalObject instance;
    private final Pane root;

    private UniversalObject() throws FileNotFoundException {
        root = new Pane();
        root.setMinWidth(3000);
        root.setMinHeight(2600);

        labelCountActiveMicroObjects.setLayoutX(1065);
        labelCountActiveMicroObjects.setLayoutY(270);
        labelCountActiveMicroObjects.setFont(Font.font("VERDANA", 16));
        updateCountActiveMicroObjects();

        String wallpaperPath = "/com/example/exampleoop/images/gameMap.jpg";
        InputStream inputStream = getClass().getResourceAsStream(wallpaperPath);
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(imageWidth);
        imageView.setFitHeight(imageHeight);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);


        root.getChildren().addAll(imageView);
    }

    public static UniversalObject getInstance() throws FileNotFoundException {
        return instance == null ? instance = new UniversalObject() : instance;
    }

    public static void askWorldWhatToDo(WalkingZombie zombie) throws FileNotFoundException {
        if (zombie.isActive()) return;

        if (zombie.getBrainCount() == 0 && zombie.getMouseCount() == 0 && zombie.getLogCount() == 0) {
            int randomNumber = new Random().nextInt(3) + 1;

            if (randomNumber == 1) {
                randomNumber = new Random().nextInt(MacroObjectManager.getForests().size());
                Forest forest = MacroObjectManager.getForests().get(randomNumber);
                if (forest.getZombies().size() <= 2) {
                    zombie.setAim(forest.getX(), forest.getY());
                }
            } else if (randomNumber == 2) {
                randomNumber = new Random().nextInt(MacroObjectManager.getCitys().size());
                City city = MacroObjectManager.getCitys().get(randomNumber);
                if (city.getWalkingZombie().size() <= 2) {
                    zombie.setAim(city.getX(), city.getY());
                }
            } else {
                randomNumber = new Random().nextInt(MacroObjectManager.getFields().size());
                Field field = MacroObjectManager.getFields().get(randomNumber);
                if (field.getWalkingZombie().size() <= 2) {
                    zombie.setAim(field.getX(), field.getY());
                }
            }
        } else if (zombie.getLocation() == MacroObjects.FOREST && zombie.getLogCount() >= 30) {
            int randomNumber = new Random().nextInt(MacroObjectManager.getCitys().size());
            City city = MacroObjectManager.getCitys().get(randomNumber);
            if (city.getWalkingZombie().size() <= 2) {
                zombie.setAim(city.getX(), city.getY());
            }
        } else if (zombie.getLocation() == MacroObjects.CITY && zombie.getBrainCount() >= 22) {
            int randomNumber = new Random().nextInt(MacroObjectManager.getFields().size());
            Field field = MacroObjectManager.getFields().get(randomNumber);
            if (field.getWalkingZombie().size() <= 2) {
                zombie.setAim(field.getX(), field.getY());
            }
        } else if (zombie.getLocation() == MacroObjects.FIELD && zombie.getMouseCount() >= 48) {
            int randomNumber = new Random().nextInt(MacroObjectManager.getForests().size());
            Forest forest = MacroObjectManager.getForests().get(randomNumber);
            if (forest.getZombies().size() <= 2) {
                zombie.setAim(forest.getX(), forest.getY());
            }
        }
        if (zombie.resourcesIsFull()) {
            try {
                zombie.setAim(Base.getInstance().getX() + 20, Base.getInstance().getY());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateCountActiveMicroObjects() {
        if (labelCountActiveMicroObjects == null) return;

        int countActiveZombies = (int) ZombieObjectManager.getZombies().stream().filter(WalkingZombie::isActive).count();

        if (countActiveZombies == 0) labelCountActiveMicroObjects.setText("Активованих зомбі немає!");
        else if (countActiveZombies == 1) {
            WalkingZombie walkingZombie = ZombieObjectManager.getZombies().stream()
                    .filter(WalkingZombie::isActive)
                    .findFirst()
                    .orElse(null);

            assert walkingZombie != null;
            labelCountActiveMicroObjects.setText("Активовано: " + walkingZombie.getName());
        } else labelCountActiveMicroObjects.setText(String.format("Активовано %d зомбі", countActiveZombies));
    }

    public static void initLabelResources() throws FileNotFoundException {
        labelCountResourcesOnBase.setLayoutX(1065);
        labelCountResourcesOnBase.setLayoutY(300);
        labelCountResourcesOnBase.setFont(Font.font("VERDANA", 16));
        updateCountResourcesOnBase();
        Main.getRoot().getChildren().add(labelCountResourcesOnBase);
    }

    public static void updateCountResourcesOnBase() throws FileNotFoundException {
        int mouseCount = Base.getInstance().getMouseCount();
        int logCount = Base.getInstance().getLogCount();
        int brainCount = Base.getInstance().getBrainCount();

        labelCountResourcesOnBase.setText("Ресурсів на базі:\n" + "Мозок: " + brainCount + "\n" +
                "Полуниця: " + logCount + "\n" + "Миша: " + mouseCount
        );
    }

    public static double getMapCoefficientCount() {
        return mapCoefficientCount;
    }

    public static int getImageWidth() {
        return imageWidth;
    }

    public static int getImageHeight() {
        return imageHeight;
    }

    public static Label getLabelCountActiveMicroObjects() {
        return labelCountActiveMicroObjects;
    }

    public Pane getRoot() {
        return root;
    }
}
