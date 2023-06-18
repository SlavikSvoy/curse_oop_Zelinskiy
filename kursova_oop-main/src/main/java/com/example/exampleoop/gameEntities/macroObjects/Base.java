package com.example.exampleoop.gameEntities.macroObjects;

import com.example.exampleoop.Main;
import com.example.exampleoop.gameEntities.UniversalObject;
import com.example.exampleoop.gameEntities.microObjects.WalkingZombie;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Base {
    private static Base base;
    private final int x = 900;
    private final int y = 900;
    private final transient List<WalkingZombie> zombies = new ArrayList<>();
    private transient Rectangle rectangle;
    private transient Label labelName;
    private transient Label labelBrainCount;
    private transient Label labelLogCount;
    private transient Label labelMouseCount;
    private transient ImageView image;
    private transient ImageView iconBrainResources;
    private transient ImageView iconLogResources;
    private transient ImageView iconMouseResources;
    private int brainCount = 0;
    private int logCount = 0;
    private int mouseCount = 0;

    private Base() throws FileNotFoundException {
        initializeGraphics();
        UniversalObject.getInstance().getRoot().getChildren().addAll(image, labelName, labelLogCount, labelBrainCount, labelMouseCount, rectangle,
                iconLogResources, iconMouseResources, iconBrainResources
        );
    }

    public static Base getInstance() throws FileNotFoundException {
        if (base == null) base = new Base();
        return base;
    }

    private void initializeGraphics() {
        labelName = new Label("База");
        labelName.setLayoutX(x + 150);
        labelName.setLayoutY(y - 40);
        labelName.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        labelName.setAlignment(Pos.TOP_CENTER);
        labelName.setTextFill(Color.GOLD);

        labelBrainCount = new Label(String.valueOf(brainCount));
        labelBrainCount.setLayoutX(x + 350);
        labelBrainCount.setLayoutY(y - 40);
        labelBrainCount.setFont(Font.font("System", FontWeight.BOLD, 19));
        labelBrainCount.setTextFill(Color.WHITE);

        labelLogCount = new Label(String.valueOf(logCount));
        labelLogCount.setLayoutX(x + 350);
        labelLogCount.setLayoutY(y - 20);
        labelLogCount.setFont(Font.font("System", FontWeight.BOLD, 19));
        labelLogCount.setTextFill(Color.WHITE);

        labelMouseCount = new Label(String.valueOf(mouseCount));
        labelMouseCount.setLayoutX(x + 350);
        labelMouseCount.setLayoutY(y);
        labelMouseCount.setFont(Font.font("System", FontWeight.BOLD, 19));
        labelMouseCount.setTextFill(Color.WHITE);

        iconBrainResources = loadImage("images/brain.png", 40);
        iconLogResources = loadImage("images/Strawberry.png", 20);
        iconMouseResources = loadImage("images/mouse.png", 0);

        rectangle = new Rectangle(x, y - 45, 400, 400);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(2);

        image = new ImageView(new Image(Objects.requireNonNull(
                Main.class.getResource("images/base.png")).toString(),
                200*2, 180*2, false, false)
        );
        image.setX(x);
        image.setY(y);
    }

    protected ImageView loadImage(String imagePath, int yOffset) {
        ImageView image = new ImageView(new Image(Objects.requireNonNull(
                Main.class.getResource(imagePath)).toString(),
                20, 20, false, false)
        );
        image.setX(x + 320);
        image.setY(y - yOffset);
        return image;
    }

    public void stIncrease(int logCount) throws FileNotFoundException {
        this.logCount += logCount;
        labelLogCount.setText(String.valueOf(this.logCount));
        UniversalObject.updateCountResourcesOnBase();
    }

    public void mouseIncrease(int mouseCount) throws FileNotFoundException {
        this.mouseCount += mouseCount;
        labelMouseCount.setText(String.valueOf(this.mouseCount));
        UniversalObject.updateCountResourcesOnBase();
    }

    public void brainIncrease(int brainCount) throws FileNotFoundException {
        this.brainCount += brainCount;
        labelBrainCount.setText(String.valueOf(this.brainCount));
        UniversalObject.updateCountResourcesOnBase();
    }

    public List<WalkingZombie> getZombies() {
        return zombies;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getBrainCount() {
        return brainCount;
    }

    public void setBrainCount(int brainCount) throws FileNotFoundException {
        this.brainCount = brainCount;
        labelBrainCount.setText(String.valueOf(brainCount));
        UniversalObject.updateCountResourcesOnBase();
    }

    public int getLogCount() {
        return logCount;
    }

    public void setLogCount(int logCount) throws FileNotFoundException {
        this.logCount = logCount;
        labelLogCount.setText(String.valueOf(logCount));
        UniversalObject.updateCountResourcesOnBase();
    }

    public int getMouseCount() {
        return mouseCount;
    }

    public void setMouseCount(int mouseCount) throws FileNotFoundException {
        this.mouseCount = mouseCount;
        labelMouseCount.setText(String.valueOf(mouseCount));
        UniversalObject.updateCountResourcesOnBase();
    }
}
