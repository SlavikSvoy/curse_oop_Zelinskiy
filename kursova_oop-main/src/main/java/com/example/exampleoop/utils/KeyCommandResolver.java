package com.example.exampleoop.utils;

import com.example.exampleoop.Main;
import com.example.exampleoop.gameEntities.MacroObjects;
import com.example.exampleoop.gameEntities.UniversalObject;
import com.example.exampleoop.gameEntities.macroObjects.Base;
import com.example.exampleoop.gameEntities.macroObjects.Field;
import com.example.exampleoop.gameEntities.macroObjects.Forest;
import com.example.exampleoop.gameEntities.macroObjects.City;
import com.example.exampleoop.gameEntities.microObjects.WalkingZombie;
import com.example.exampleoop.gameEntities.microObjects.RottingZombie;
import com.example.exampleoop.services.MacroObjectManager;
import com.example.exampleoop.services.ZombieObjectManager;
import javafx.scene.input.KeyCode;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class KeyCommandResolver {
    private static final Logger log = Logger.getLogger(KeyCommandResolver.class);

    public static void setKeyboardHandlers() {
        Main.getScene().setOnKeyPressed(event -> {
            List<WalkingZombie> activeZombies = ZombieObjectManager.getZombies().stream()
                    .filter(WalkingZombie::isActive)
                    .collect(Collectors.toList());

            if (event.getCode() == KeyCode.ESCAPE) {
                for (WalkingZombie walkingZombie : activeZombies) {
                    walkingZombie.setActive(!walkingZombie.isActive());
                    UniversalObject.updateCountActiveMicroObjects();
                }
            }

            if (event.getCode() == KeyCode.DELETE) {
                activeZombies.forEach(zombie -> {
                    int index = ZombieObjectManager.getZombies().indexOf(zombie);
                    log.info("Видалено зомбі " + zombie.toString() + " за індексом " + index);
                    ZombieObjectManager.removeZombie(index);
                });
            }

            if (event.getCode() == KeyCode.J) {
                activeZombies.stream().map(WalkingZombie::clone).forEach(zombie -> {
                    log.info("Додано клонованого зомбі " + zombie.toString());
                    ZombieObjectManager.addZombie(zombie);
                });
            }

            if (event.getCode() == KeyCode.T) {
                ZombieObjectManager.getZombies().stream()
                        .filter(zombie -> zombie instanceof RottingZombie)
                        .forEach(zombie -> zombie.setActive(true));
            }

            if (event.getCode() == KeyCode.B) {
                try {
                    ZombieObjectManager.goZombiesOnBase();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            ZombieObjectManager.getZombies().forEach(KeyCommandResolver::detercityZombieLocation);
            handleZombieMovement(activeZombies, event.getCode());
        });
    }

    private static void handleZombieMovement(List<WalkingZombie> zombies, KeyCode keyCode) {
        for (WalkingZombie zombie : zombies) {
            switch (keyCode) {
                case D -> {
                    zombie.setDirection(-1);
                    zombie.setPosition(zombie.getX() + zombie.getSpeed(), zombie.getY());
                    zombie.getImage().setScaleX(zombie.getDirection());
                }
                case A -> {
                    zombie.setDirection(1);
                    zombie.setPosition(zombie.getX() - zombie.getSpeed(), zombie.getY());
                    zombie.getImage().setScaleX(zombie.getDirection());
                }
                case W -> zombie.setPosition(zombie.getX(), zombie.getY() - zombie.getSpeed());
                case S -> zombie.setPosition(zombie.getX(), zombie.getY() + zombie.getSpeed());
            }
        }
    }

    public static void detercityZombieLocation(WalkingZombie zombie) {
        boolean isInForest = false;
        for (Forest forest : MacroObjectManager.getForests()) {
            if (zombie.getImage().getBoundsInParent().intersects(forest.getRectangle().getBoundsInParent())) {
                zombie.setLocation(MacroObjects.FOREST);
                forest.getZombies().add(zombie);
                forest.getZombies().add(zombie);
                log.info("Зомбі " + zombie.getName() + " увійшов у Ліс");
                isInForest = true;
                break;
            }
        }

        boolean isInField = false;
        for (Field field : MacroObjectManager.getFields()) {
            if (zombie.getImage().getBoundsInParent().intersects(field.getRectangle().getBoundsInParent())) {
                zombie.setLocation(MacroObjects.FIELD);
                field.getWalkingZombie().add(zombie);
                field.getWalkingZombie().add(zombie);
                log.info("Зомбі " + zombie.getName() + " увійшов у Поле");
                zombie.setProcessing(true);
                isInField = true;
                break;
            }
        }

        boolean isInCity = false;
        for (City city : MacroObjectManager.getCitys()) {
            if (zombie.getImage().getBoundsInParent().intersects(city.getRectangle().getBoundsInParent())) {
                zombie.setLocation(MacroObjects.CITY);
                city.getWalkingZombie().add(zombie);
                zombie.setProcessing(true);
                log.info("Зомбі " + zombie.getName() + " увійшов у Місто");
                isInCity = true;
                break;
            }
        }

        boolean isInBase = false;
        try {
            if (zombie.getImage().getBoundsInParent().intersects(Base.getInstance().getRectangle().getBoundsInParent())) {
                zombie.setLocation(MacroObjects.BASE);
                Base.getInstance().getZombies().add(zombie);
                zombie.setProcessing(true);
                isInBase = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (!isInForest && !isInField && !isInCity && !isInBase) {
            zombie.setLocation(MacroObjects.MAP);
            MacroObjectManager.getCitys().forEach(city -> city.getWalkingZombie().clear());
            MacroObjectManager.getFields().forEach(field -> field.getWalkingZombie().clear());
            MacroObjectManager.getForests().forEach(forest -> forest.getZombies().clear());
            zombie.setProcessing(false);
        }
    }
}
