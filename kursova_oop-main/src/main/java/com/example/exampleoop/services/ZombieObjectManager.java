package com.example.exampleoop.services;

import com.example.exampleoop.gameEntities.MacroObjects;
import com.example.exampleoop.gameEntities.UniversalObject;
import com.example.exampleoop.gameEntities.macroObjects.Base;
import com.example.exampleoop.gameEntities.microObjects.WalkingZombie;
import com.example.exampleoop.utils.DialogUtils;
import com.example.exampleoop.utils.SoundPlayer;
import javafx.scene.control.Alert;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ZombieObjectManager {
    private static final List<WalkingZombie> zombies = new ArrayList<>();

    public static void addZombie(WalkingZombie zombie) {
        zombies.add(zombie);
    }

    public static void removeZombie(int index) {
        if (index >= 0 && index < zombies.size()) {
            zombies.get(index).setVisible();
            zombies.remove(index);

            new SoundPlayer().playSound(SoundPlayer.ZOMBIE_DIE_SOUND_PATH, false);
            DialogUtils.showAlert("Ви успішно видалили мікрооб'єкт!", Alert.AlertType.INFORMATION);
            UniversalObject.updateCountActiveMicroObjects();
        }
    }

    public static void goZombiesOnBase() throws FileNotFoundException {
        double x = Base.getInstance().getX();
        double y = Base.getInstance().getY();

        zombies.stream()
                .filter(zombie -> !zombie.isActive())
                .peek(zombie -> zombie.setAim(x, y))
                .forEach(WalkingZombie::autoMove);
    }

    public static void processZombieActions(WalkingZombie zombie) {
        if (zombie.getLocation().equals(MacroObjects.CITY)) {
            zombie.brainDig();
        } else if (zombie.getLocation().equals(MacroObjects.FIELD)) {
            zombie.mouseDig();
        } else if (zombie.getLocation().equals(MacroObjects.FOREST)) {
            zombie.stDig();
        } else if (zombie.getLocation().equals(MacroObjects.BASE)) {
            try {
                zombie.decreaseResourcesOnBase();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> getNames() {
        return zombies.stream().map(WalkingZombie::toString).collect(Collectors.toList());
    }

    public static void removeAllZombies() {
        zombies.forEach(WalkingZombie::setVisible);
        zombies.clear();
    }

    public static void lifeCycle() {
        zombies.forEach(WalkingZombie::autoMove);
    }

    public static void filterZombies(Predicate<WalkingZombie> condition) {
        System.out.println("Result of searching: ");
        zombies.stream().filter(condition).forEach(System.out::println);
    }

    public static void sortZombies(Comparator<WalkingZombie> comparator) {
        WalkingZombie[] walkingZombie = ZombieObjectManager.zombies.toArray(new WalkingZombie[0]);
        Arrays.sort(walkingZombie, comparator);
        System.out.println(Arrays.toString(walkingZombie));
    }

    public static void countFilteredZombies(Predicate<WalkingZombie> condition) {
        long countFilteredZombies = zombies.stream().filter(condition).count();
        System.out.println("Count of filtered zombies: " + countFilteredZombies);
    }

    public static List<WalkingZombie> getZombies() {
        return zombies;
    }
}
