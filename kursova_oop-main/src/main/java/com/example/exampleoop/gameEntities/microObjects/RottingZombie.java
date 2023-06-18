package com.example.exampleoop.gameEntities.microObjects;

import com.example.exampleoop.Main;
import com.example.exampleoop.gameEntities.ZombieLevel;
import com.example.exampleoop.gameEntities.macroObjects.Base;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;
import java.util.Objects;

public class RottingZombie extends RunningZombie {
    public RottingZombie(String name, int health, double speed, double x, double y, String[] inventory, boolean isActive) throws FileNotFoundException {
        super(name, health, speed, x, y, inventory, isActive);
        zombieLevel = ZombieLevel.ROTTING;
    }

    public RottingZombie() throws FileNotFoundException {
        super();
        zombieLevel = ZombieLevel.ROTTING;
    }

    @Override
    public void decreaseResourcesOnBase() throws FileNotFoundException {
        if (brainCount > 0) {
            int brainsToAdd = Math.min(brainCount, 4);
            brainCount -= brainsToAdd;
            labelBrainCount.setText(String.valueOf(brainCount));
            Base.getInstance().brainIncrease(brainsToAdd);
        }

        if (mouseCount > 0) {
            int mouseToAdd = Math.min(mouseCount, 4);
            mouseCount -= mouseToAdd;
            labelMouseCount.setText(String.valueOf(mouseCount));
            Base.getInstance().mouseIncrease(mouseToAdd);
        }

        if (logCount > 0) {
            int logsToAdd = Math.min(logCount, 4);
            logCount -= logsToAdd;
            labelLogCount.setText(String.valueOf(logCount));
            Base.getInstance().stIncrease(logsToAdd);
        }
    }

    @Override
    protected void loadMainImage() {
        this.setImage(new ImageView(new Image(Objects.requireNonNull(
                Main.class.getResource("images/RottingZombie.png")).toString(),
                200, 180, false, false)
        ));
        this.getImage().setX(x);
        this.getImage().setY(y);
    }

    @Override
    public void brainDig() {
        if (checkValidResourcesCount(brainCount, 32)) return;
        isProcessing = true;
        brainCount += 2;
        super.getLabelBrainCount().setText(String.valueOf(brainCount));
    }

    @Override
    public void mouseDig() {
        if (checkValidResourcesCount(mouseCount, 60)) return;
        isProcessing = true;
        mouseCount += 5;
        super.getLabelMouseCount().setText(String.valueOf(mouseCount));
    }

    @Override
    public void stDig() {
        if (checkValidResourcesCount(logCount, 32)) return;
        isProcessing = true;
        logCount += 4;
        super.getLabelLogCount().setText(String.valueOf(logCount));
    }

    @Override
    public String toString() {
        return "RottingZombie{" +
                "name='" + this.name + '\'' +
                ", health=" + this.health +
                ", speed=" + this.speed +
                '}';
    }
}
