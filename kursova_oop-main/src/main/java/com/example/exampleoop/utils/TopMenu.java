package com.example.exampleoop.utils;

import com.example.exampleoop.gameEntities.MacroObjects;
import com.example.exampleoop.gameEntities.microObjects.WalkingZombie;
import com.example.exampleoop.services.ZombieObjectManager;
import com.example.exampleoop.zombieDialogs.GameHelpDlg;
import com.example.exampleoop.zombieDialogs.ZombieSearchDlg;
import com.example.exampleoop.zombieDialogs.ZombieSelectionDlg;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.io.FileNotFoundException;

public class TopMenu {
    private final Group root;

    public TopMenu() {
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-font-family: 'Roboto'; -fx-font-size: 13px; -fx-background-color: #f2f2f2;");

        // Menu Button File
        Menu fileMenu = new Menu("File");

        MenuItem openFile = new MenuItem("Open");
        fileMenu.getItems().add(openFile);
        openFile.setOnAction(actionEvent -> SerializationUtils.deserialize());

        MenuItem saveFile = new MenuItem("Save");
        fileMenu.getItems().add(saveFile);
        saveFile.setOnAction(actionEvent -> {
            try {
                SerializationUtils.serialize();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        MenuItem exitFile = new MenuItem("Exit");
        exitFile.setOnAction(actionEvent -> Platform.exit());
        fileMenu.getItems().add(exitFile);

        // Menu Button Edit
        Menu editMenu = new Menu("Edit");
        MenuItem changeZombieMenu = new MenuItem("Change zombie");
        changeZombieMenu.setOnAction(actionEvent -> new ZombieSelectionDlg().display());
        editMenu.getItems().add(changeZombieMenu);

        // Menu Button Object Operations
        Menu objectOperationsMenu = new Menu("Zombie operations");

        MenuItem findZombieByParameters = new MenuItem("Find zombie by parameters");
        findZombieByParameters.setOnAction(actionEvent -> new ZombieSearchDlg().display());
        objectOperationsMenu.getItems().add(findZombieByParameters);

        MenuItem findDetachedMicroObjects = new MenuItem("Find detached zombies");
        objectOperationsMenu.getItems().add(findDetachedMicroObjects);
        findDetachedMicroObjects.setOnAction(actionEvent -> ZombieObjectManager.filterZombies(zombie -> MacroObjects.MAP.equals(zombie.getLocation())));

        Menu attachedMicroObjectsMenu = new Menu("Find attached zombies");
        MenuItem optionCity = new MenuItem("City");
        MenuItem optionForest = new MenuItem("Forest");
        MenuItem optionField = new MenuItem("Field");
        attachedMicroObjectsMenu.getItems().addAll(optionCity, optionForest, optionField);
        objectOperationsMenu.getItems().add(attachedMicroObjectsMenu);
        optionCity.setOnAction(actionEvent -> ZombieObjectManager.filterZombies(zombie -> MacroObjects.CITY.equals(zombie.getLocation())));
        optionForest.setOnAction(actionEvent -> ZombieObjectManager.filterZombies(zombie -> MacroObjects.FOREST.equals(zombie.getLocation())));
        optionField.setOnAction(actionEvent -> ZombieObjectManager.filterZombies(zombie -> MacroObjects.FIELD.equals(zombie.getLocation())));

        MenuItem findActiveMicroObjects = new MenuItem("Find active zombies");
        objectOperationsMenu.getItems().add(findActiveMicroObjects);
        findActiveMicroObjects.setOnAction(actionEvent -> ZombieObjectManager.filterZombies(WalkingZombie::isActive));

        MenuItem countActiveMicroObjects = new MenuItem("Find count of active zombies");
        objectOperationsMenu.getItems().add(countActiveMicroObjects);
        countActiveMicroObjects.setOnAction(actionEvent -> ZombieObjectManager.countFilteredZombies(WalkingZombie::isActive));

        MenuItem countHighHealthMicroObjects = new MenuItem("Find count of high health zombies");
        objectOperationsMenu.getItems().add(countHighHealthMicroObjects);
        countHighHealthMicroObjects.setOnAction(actionEvent -> ZombieObjectManager.countFilteredZombies(zombie -> zombie.getHealth() > 70));

        Menu sortMenu = new Menu("Sort zombies");
        MenuItem optionSpeedSort = new MenuItem("Sort by speed");
        MenuItem optionLogSort = new MenuItem("Sort by st count");
        MenuItem optionMouseSort = new MenuItem("Sort by mouse count");
        MenuItem optionBrainSort = new MenuItem("Sort by brain count");
        sortMenu.getItems().addAll(optionSpeedSort, optionLogSort, optionMouseSort, optionBrainSort);
        objectOperationsMenu.getItems().addAll(sortMenu);
        optionSpeedSort.setOnAction(actionEvent -> ZombieObjectManager.sortZombies(new WalkingZombie.SpeedComparator()));
        optionLogSort.setOnAction(actionEvent -> ZombieObjectManager.sortZombies(new WalkingZombie.LogCountComparator()));
        optionMouseSort.setOnAction(actionEvent -> ZombieObjectManager.sortZombies(new WalkingZombie.MouseCountComparator()));
        optionBrainSort.setOnAction(actionEvent -> ZombieObjectManager.sortZombies(new WalkingZombie.BrainCountComparator()));

        // Menu Button Help
        Menu helpMenu = new Menu("Help");
        MenuItem info = new MenuItem("FAQ");
        helpMenu.getItems().add(info);
        helpMenu.setOnAction(actionEvent -> new GameHelpDlg().display());

        menuBar.getMenus().addAll(fileMenu, editMenu, objectOperationsMenu, helpMenu);
        root = new Group(menuBar);
    }

    public Group getRoot() {
        return root;
    }
}