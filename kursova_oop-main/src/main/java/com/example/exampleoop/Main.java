package com.example.exampleoop;

import com.example.exampleoop.gameEntities.UniversalObject;
import com.example.exampleoop.gameEntities.macroObjects.Base;
import com.example.exampleoop.gameEntities.macroObjects.Field;
import com.example.exampleoop.gameEntities.macroObjects.Forest;
import com.example.exampleoop.gameEntities.macroObjects.City;
import com.example.exampleoop.gameEntities.microObjects.RunningZombie;
import com.example.exampleoop.gameEntities.microObjects.WalkingZombie;
import com.example.exampleoop.gameEntities.microObjects.RottingZombie;
import com.example.exampleoop.services.MacroObjectManager;
import com.example.exampleoop.services.ZombieObjectManager;
import com.example.exampleoop.utils.KeyCommandResolver;
import com.example.exampleoop.utils.MiniMap;
import com.example.exampleoop.utils.TopMenu;
import com.example.exampleoop.zombieDialogs.ZombieCreateDlg;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.FileNotFoundException;

public class Main extends Application {
    private static final Group group = new Group();
    private static final Group root = new Group();
    private static Stage stage;
    private static Scene scene;
    private static ScrollPane scrollPane;

    public static void main(String[] args) {
        launch();
    }

    public static Stage getStage() {
        return stage;
    }

    public static Group getRoot() {
        return root;
    }

    public static Scene getScene() {
        return scene;
    }

    public static ScrollPane getScrollPane() {
        return scrollPane;
    }

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        Main.stage = stage;
//        new SoundPlayer().playSound(SoundPlayer.BACKGROUND_MUSIC_PATH, true);

        group.getChildren().add(UniversalObject.getInstance().getRoot());
        scrollPane = new ScrollPane(group);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        scrollPane.setPrefSize(screenSize.getWidth(), screenSize.getHeight());

        MiniMap miniMap = new MiniMap();
        root.getChildren().addAll(scrollPane, miniMap.getGroup(), new TopMenu().getRoot(), UniversalObject.getLabelCountActiveMicroObjects());
        scene = new Scene(root, 1350, 750);

        stage.setTitle("The Walking Dead");
        stage.setScene(scene);
        stage.show();

        scrollPane.viewportBoundsProperty().addListener((observable, oldBounds, bounds) -> {
            double scrollX = -1 * (int) bounds.getMaxX() * miniMap.getMinimapScale() + 1205;
            double scrollY = -1 * (int) bounds.getMinY() * miniMap.getMinimapScale();

            miniMap.updateActiveAreaRect(scrollX, scrollY);
            group.setLayoutX(group.getLayoutX() + 1);
            group.setLayoutY(group.getLayoutY() + 1);
        });

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                    for (WalkingZombie walkingZombie : ZombieObjectManager.getZombies()) {
                        KeyCommandResolver.detercityZombieLocation(walkingZombie);
                        ZombieObjectManager.lifeCycle();
                        ZombieObjectManager.processZombieActions(walkingZombie);
                    }
                }));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                ZombieObjectManager.lifeCycle();
            }
        };
        timer.start();

        scene.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.INSERT) {
                double mouseX = MouseInfo.getPointerInfo().getLocation().getX();
                double mouseY = MouseInfo.getPointerInfo().getLocation().getY();
                new ZombieCreateDlg().display(mouseX, mouseY);
            }
        });

        MacroObjectManager.addMacroObject(new Field(100, 100));
        MacroObjectManager.addMacroObject(new Field(100, 800));
        MacroObjectManager.addMacroObject(new Field(100, 1950));
        MacroObjectManager.addMacroObject(new Forest(2000, 1800));
        MacroObjectManager.addMacroObject(new Forest(1900, 600));
        MacroObjectManager.addMacroObject(new City(1400, 100));
        MacroObjectManager.addMacroObject(new City(2600, 1700));
        MacroObjectManager.addMacroObject(new City(850, 2000));
        Base.getInstance();
        UniversalObject.initLabelResources();
        miniMap.updateActiveAreaRect(1040, 4);

        ZombieObjectManager.addZombie(new RunningZombie("Zephyr", 100, 7, 1500, 400, new String[]{"Мозок"}, false));
        ZombieObjectManager.addZombie(new RottingZombie("Azalea", 70, 10, 800, 500, new String[]{"Мозок"}, false));
        ZombieObjectManager.addZombie(new RottingZombie("Calyx", 30, 10, 1000, 100, new String[]{"Мозок"}, false));
        ZombieObjectManager.addZombie(new WalkingZombie("Larkspur", 55, 4, 700, 150, new String[]{"Мозок"}, false));
        ZombieObjectManager.addZombie(new WalkingZombie("Solstice", 20, 4, 400, 500, new String[]{"Мозок"}, false));
    }
}