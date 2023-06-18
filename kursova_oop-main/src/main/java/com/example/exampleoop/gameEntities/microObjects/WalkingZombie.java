package com.example.exampleoop.gameEntities.microObjects;

import com.example.exampleoop.Main;
import com.example.exampleoop.gameEntities.MacroObjects;
import com.example.exampleoop.gameEntities.UniversalObject;
import com.example.exampleoop.gameEntities.ZombieLevel;
import com.example.exampleoop.gameEntities.macroObjects.Base;
import com.example.exampleoop.utils.DialogUtils;
import com.example.exampleoop.utils.KeyCommandResolver;
import com.example.exampleoop.utils.SoundPlayer;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import static javafx.util.Duration.millis;

public class WalkingZombie implements Comparable<WalkingZombie>, Cloneable, Comparator<WalkingZombie> {
    protected static final double IMAGE_SIZE = 220 * UniversalObject.getMapCoefficientCount();
    protected static final double HEALTH_HEIGHT = 8 * UniversalObject.getMapCoefficientCount();
    protected static final double FONT_SIZE = 14 * UniversalObject.getMapCoefficientCount();
    protected static final Rectangle2D.Double UNIT_CONTAINER_BOUNDS = new Rectangle2D.Double(0, 0, IMAGE_SIZE, IMAGE_SIZE + HEALTH_HEIGHT + FONT_SIZE * 1.3 + 5);
    protected static final Point MAX_UNIT = new Point((int) (3000 - 5 - UNIT_CONTAINER_BOUNDS.width), (int) (2600 - 5 - UNIT_CONTAINER_BOUNDS.height));
    protected static final Point MIN_UNIT = new Point(5, 5);
    private static final Logger log = Logger.getLogger(WalkingZombie.class);

    static {
        DialogUtils.showAlert("Гра розпочалась!", Alert.AlertType.INFORMATION);
    }

    protected ZombieLevel zombieLevel;
    protected String name;
    protected String[] inventory;
    protected int health;
    protected int brainCount = 0;
    protected int logCount = 0;
    protected int mouseCount = 0;
    protected int direction = 1;
    protected double speed;
    protected double x;
    protected double y;
    protected double aimX;
    protected double aimY;
    protected boolean isActive;
    protected boolean isProcessing;
    protected MacroObjects location = MacroObjects.MAP;
    protected transient Label labelBrainCount;
    protected transient Label labelLogCount;
    protected transient Label labelMouseCount;
    private transient Label labelName;
    private transient Label labelInventory;
    private transient ImageView image;
    private transient ImageView iconBrainResources;
    private transient ImageView iconLogResources;
    private transient ImageView iconMouseResources;
    private transient Line lineOfHealth;
    private transient Line lineOfMaxHealth;

    {
        log.info("Створення нового зомбі: " + this);
    }

    public WalkingZombie(String name, int health, double speed, double x, double y, String[] inventory, boolean isActive) throws FileNotFoundException {
        this.isActive = false;
        this.name = name;
        this.health = health;
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.inventory = Arrays.copyOf(inventory, inventory.length);
        this.zombieLevel = ZombieLevel.WALKING;
        clearAim();

        initializeGraphics();
        KeyCommandResolver.setKeyboardHandlers();
        setEventHandlers();
        setActive(isActive);
        makeSound();

        UniversalObject.getInstance().getRoot().getChildren().addAll(image, labelName, lineOfMaxHealth, lineOfHealth,
                labelInventory, labelBrainCount, labelLogCount, labelMouseCount,
                iconBrainResources, iconLogResources, iconMouseResources);
    }

    public WalkingZombie() throws FileNotFoundException {
        this("Zombie", 60, 6, 350, 300, new String[]{"Камінець"}, false);
    }

    protected void initializeGraphics() {
        labelName = new Label(name);
        labelName.setLayoutX(x + 70);
        labelName.setLayoutY(y - 40);
        labelName.setFont(Font.font("System", FontWeight.BOLD, 14));
        labelName.setTextFill(Color.WHITE);

        labelInventory = new Label(Arrays.toString(inventory));
        labelInventory.setLayoutX(x + 70);
        labelInventory.setLayoutY(y - 60);
        labelInventory.setFont(Font.font("System", FontWeight.BOLD, 14));
        labelInventory.setTextFill(Color.LIGHTGOLDENRODYELLOW);

        labelBrainCount = new Label(String.valueOf(brainCount));
        labelBrainCount.setLayoutX(x + 195);
        labelBrainCount.setLayoutY(y - 40);
        labelBrainCount.setFont(Font.font("System", FontWeight.BOLD, 14));
        labelBrainCount.setTextFill(Color.WHITE);

        labelLogCount = new Label(String.valueOf(logCount));
        labelLogCount.setLayoutX(x + 195);
        labelLogCount.setLayoutY(y - 15);
        labelLogCount.setFont(Font.font("System", FontWeight.BOLD, 14));
        labelLogCount.setTextFill(Color.WHITE);

        labelMouseCount = new Label(String.valueOf(mouseCount));
        labelMouseCount.setLayoutX(x + 195);
        labelMouseCount.setLayoutY(y + 10);
        labelMouseCount.setFont(Font.font("System", FontWeight.BOLD, 14));
        labelMouseCount.setTextFill(Color.WHITE);

        lineOfMaxHealth = new Line(x + 55, y - 11, x + 155, y - 11);
        lineOfMaxHealth.setStrokeWidth(8);
        lineOfMaxHealth.setStroke(Color.LIGHTGRAY);

        lineOfHealth = new Line(x + 55, y - 11, x + health + 55, y - 11);
        lineOfHealth.setStrokeWidth(7);
        lineOfHealth.setStroke(Color.ORANGERED);

        iconBrainResources = loadImage("images/brain.png", 170, 40);
        iconLogResources = loadImage("images/Strawberry.png", 170, 15);
        iconMouseResources = loadImage("images/mouse.png", 170, -10);

        loadMainImage();
    }

    protected void loadMainImage() {
        image = new ImageView(new Image(Objects.requireNonNull(
                Main.class.getResource("images/walkingZombie.png")).toString(),
                200, 180, false, false)
        );
        image.setX(x);
        image.setY(y);
    }

    protected ImageView loadImage(String imagePath, int xOffset, int yOffset) {
        ImageView image = new ImageView(new Image(Objects.requireNonNull(
                Main.class.getResource(imagePath)).toString(),
                20, 20, false, false)
        );
        image.setX(x + xOffset);
        image.setY(y - yOffset);
        return image;
    }

    protected void setEventHandlers() {
        image.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(millis(100), image);
            scaleTransition.setToX(1.1 * direction);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
        });

        image.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(millis(100), image);
            scaleTransition.setToX(direction);
            scaleTransition.setToY(1);
            scaleTransition.play();
        });

        image.setOnMouseClicked(mouseEvent -> {
            setActive(!isActive);
            if (isActive) UniversalObject.updateCountActiveMicroObjects();
            else UniversalObject.updateCountActiveMicroObjects();
        });
    }

    public void setVisible() {
        boolean isVisible = false;
        labelName.setVisible(isVisible);
        labelInventory.setVisible(isVisible);
        image.setVisible(isVisible);
        lineOfHealth.setVisible(isVisible);
        lineOfMaxHealth.setVisible(isVisible);
        labelBrainCount.setVisible(isVisible);
        labelMouseCount.setVisible(isVisible);
        labelLogCount.setVisible(isVisible);
        iconMouseResources.setVisible(isVisible);
        iconBrainResources.setVisible(isVisible);
        iconLogResources.setVisible(isVisible);
        setActive(isVisible);
    }

    public void setPosition(double x, double y) {
        log.info("Зміна координат зомбі " + "'" + name + "'" +
                " зі " + "{x=" + this.x + ", " + "y=" + this.y + "} на " + "{x=" + x + ", " + "y=" + y + "}");
        this.x = Math.max(Math.min(x, MAX_UNIT.x), MIN_UNIT.x);
        this.y = Math.max(Math.min(y, MAX_UNIT.y), MIN_UNIT.y);

        labelName.setLayoutX(x + 70);
        labelName.setLayoutY(y - 40);

        labelInventory.setLayoutX(x + 70);
        labelInventory.setLayoutY(y - 60);

        lineOfMaxHealth.setStartX(x + 55);
        lineOfMaxHealth.setEndX(x + 155);
        lineOfMaxHealth.setStartY(y - 11);
        lineOfMaxHealth.setEndY(y - 11);

        lineOfHealth.setStartX(x + 55);
        lineOfHealth.setEndX(x + health + 55);
        lineOfHealth.setStartY(y - 11);
        lineOfHealth.setEndY(y - 11);

        iconBrainResources.setX(x + 170);
        iconBrainResources.setY(y - 40);
        labelBrainCount.setLayoutX(x + 195);
        labelBrainCount.setLayoutY(y - 40);

        iconLogResources.setX(x + 170);
        iconLogResources.setY(y - 15);
        labelLogCount.setLayoutX(x + 195);
        labelLogCount.setLayoutY(y - 15);

        iconMouseResources.setX(x + 170);
        iconMouseResources.setY(y + 10);
        labelMouseCount.setLayoutX(x + 195);
        labelMouseCount.setLayoutY(y + 10);

        image.setX(x);
        image.setY(y);
    }

    public void makeSound() {
        new SoundPlayer().playSound(SoundPlayer.ZOMBIE_SOUND_PATH, false);
    }

    public void brainDig() {
        if (checkValidResourcesCount(brainCount, 25)) return;
        isProcessing = true;
        brainCount += 1;
        labelBrainCount.setText(String.valueOf(brainCount));
    }

    public void mouseDig() {
        if (checkValidResourcesCount(mouseCount, 50)) return;
        mouseCount += 1;
        isProcessing = true;
        labelMouseCount.setText(String.valueOf(mouseCount));
    }

    public void stDig() {
        if (checkValidResourcesCount(logCount, 30)) return;
        logCount += 1;
        isProcessing = true;
        labelLogCount.setText(String.valueOf(logCount));
    }

    public void decreaseResourcesOnBase() throws FileNotFoundException {
        if (brainCount > 0) {
            brainCount -= 1;
            labelBrainCount.setText(String.valueOf(brainCount));
            Base.getInstance().brainIncrease(1);
        }
        if (mouseCount > 0) {
            mouseCount -= 1;
            labelMouseCount.setText(String.valueOf(mouseCount));
            Base.getInstance().mouseIncrease(1);
        }
        if (logCount > 0) {
            logCount -= 1;
            labelLogCount.setText(String.valueOf(logCount));
            Base.getInstance().stIncrease(1);
        }
    }

    public void autoMove() {
        if (isActive) return;

        if (isEmptyAim()) {
            try {
                UniversalObject.askWorldWhatToDo(this);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if ((Math.abs(x - aimX) + Math.abs(y - aimY)) < 27.0) {
            clearAim();
        } else if (this instanceof RottingZombie && (Math.abs(x - aimX) + Math.abs(y - aimY)) < 25.0) {
            clearAim();
        } else simpleMove(aimX, aimY);
    }

    private void simpleMove(double x, double y) {
        double dx = 0;
        double dy = 0;
        int dir = 0;

        if (x < this.x - speed) {
            dx = -speed;
            dir = 1;
        } else if (x > this.x + speed) {
            dx = speed;
            dir = -1;
        }
        if (y > this.y + speed) {
            dy = speed;
        } else if (y < this.y - speed) {
            dy = -speed;
        }
        move(dx, dy, dir);
    }

    private void move(double dx, double dy, int dir) {
        double finalDX = x + dx;
        double finalDY = y + dy;

        setPosition(Math.max(Math.min(finalDX, MAX_UNIT.x), MIN_UNIT.x), Math.max(Math.min(finalDY, MAX_UNIT.y), MIN_UNIT.y));

        if (dir != 0) {
            direction = dir;
            image.setScaleX(direction);
        }
    }

    public void setAim(double aimX, double aimY) {
        this.aimX = aimX;
        this.aimY = aimY;
    }

    private boolean isEmptyAim() {
        return (aimX < 0) && (aimY < 0);
    }

    private void clearAim() {
        aimX = aimY = -1000.0;
    }

    public boolean checkValidResourcesCount(int resourcesCount, int expectedCount) {
        if (resourcesCount >= expectedCount) {
            isProcessing = false;
            return true;
        }
        return false;
    }

    public boolean resourcesIsFull() {
        return getMouseCount() >= 40 && getLogCount() >= 20 && getBrainCount() >= 20;
    }

    @Override
    public WalkingZombie clone() {
        try {
            WalkingZombie zombie = (WalkingZombie) super.clone();
            zombie.labelName = new Label();
            zombie.labelInventory = new Label();
            zombie.labelLogCount = new Label();
            zombie.labelBrainCount = new Label();
            zombie.labelMouseCount = new Label();

            zombie.image = new ImageView();
            zombie.iconBrainResources = new ImageView();
            zombie.iconMouseResources = new ImageView();
            zombie.iconLogResources = new ImageView();
            zombie.lineOfHealth = new Line();
            zombie.lineOfMaxHealth = new Line();

            zombie.inventory = Arrays.copyOf(inventory, inventory.length);
            zombie.setName(zombie.name + " cloned");
            zombie.isActive = false;

            zombie.initializeGraphics();
            zombie.setEventHandlers();
            KeyCommandResolver.setKeyboardHandlers();
            setActive(isActive);

            UniversalObject.getInstance().getRoot().getChildren().addAll(zombie.image, zombie.labelName,
                    zombie.lineOfMaxHealth, zombie.lineOfHealth, zombie.labelInventory,
                    zombie.labelBrainCount, zombie.labelLogCount, zombie.labelMouseCount,
                    zombie.iconBrainResources, zombie.iconLogResources, zombie.iconMouseResources);
            return zombie;
        } catch (CloneNotSupportedException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int compare(WalkingZombie zombie1, WalkingZombie zombie2) {
        return Integer.compare(zombie1.getHealth(), zombie2.getHealth());
    }

    @Override
    public int compareTo(WalkingZombie walkingZombie) {
        return this.name.compareTo(walkingZombie.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WalkingZombie zombie = (WalkingZombie) o;
        return health == zombie.health &&
                brainCount == zombie.brainCount &&
                logCount == zombie.logCount &&
                mouseCount == zombie.mouseCount &&
                direction == zombie.direction &&
                Double.compare(zombie.speed, speed) == 0 &&
                Double.compare(zombie.x, x) == 0 &&
                Double.compare(zombie.y, y) == 0 &&
                Double.compare(zombie.aimX, aimX) == 0 &&
                Double.compare(zombie.aimY, aimY) == 0 &&
                isActive == zombie.isActive &&
                isProcessing == zombie.isProcessing &&
                zombieLevel == zombie.zombieLevel &&
                Objects.equals(name, zombie.name) &&
                Arrays.equals(inventory, zombie.inventory) &&
                location == zombie.location;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(zombieLevel, name, health, brainCount, logCount, mouseCount,
                direction, speed, x, y, aimX, aimY, isActive, isProcessing, location
        );
        return 31 * result + Arrays.hashCode(inventory);
    }

    @Override
    public String toString() {
        return "WalkingZombie{" +
                "name='" + name + '\'' +
                ", health=" + health +
                ", speed=" + speed +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        labelName.setText(name);
    }

    public String[] getInventory() {
        return inventory;
    }

    public void setInventory(String[] tools) {
        this.inventory = Arrays.copyOf(tools, tools.length);
        labelInventory.setText(Arrays.toString(tools));
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getBrainCount() {
        return brainCount;
    }

    public void setBrainCount(int count) {
        labelBrainCount.setText(String.valueOf(count));
    }

    public int getLogCount() {
        return logCount;
    }

    public void setLogCount(int count) {
        labelLogCount.setText(String.valueOf(count));
    }

    public int getMouseCount() {
        return mouseCount;
    }

    public void setMouseCount(int count) {
        labelMouseCount.setText(String.valueOf(count));
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);

        if (isActive) {
            dropShadow.setColor(Color.RED);
            image.setEffect(dropShadow);
        } else {
            dropShadow.setColor(Color.TRANSPARENT);
            image.setEffect(dropShadow);
        }
        log.info("Зомбі " + this + " став " + isActive);
    }

    public void setProcessing(boolean processing) {
        isProcessing = processing;
    }

    public MacroObjects getLocation() {
        return location;
    }

    public void setLocation(MacroObjects location) {
        this.location = location;
    }

    public Label getLabelBrainCount() {
        return labelBrainCount;
    }

    public Label getLabelLogCount() {
        return labelLogCount;
    }

    public Label getLabelMouseCount() {
        return labelMouseCount;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public static class SpeedComparator implements Comparator<WalkingZombie> {
        @Override
        public int compare(WalkingZombie zombie1, WalkingZombie zombie2) {
            return Double.compare(zombie2.speed, zombie1.speed);
        }
    }

    public static class MouseCountComparator implements Comparator<WalkingZombie> {
        @Override
        public int compare(WalkingZombie zombie1, WalkingZombie zombie2) {
            return Double.compare(zombie2.mouseCount, zombie1.mouseCount);
        }
    }

    public static class LogCountComparator implements Comparator<WalkingZombie> {
        @Override
        public int compare(WalkingZombie zombie1, WalkingZombie zombie2) {
            return Double.compare(zombie2.logCount, zombie1.logCount);
        }
    }

    public static class BrainCountComparator implements Comparator<WalkingZombie> {
        @Override
        public int compare(WalkingZombie zombie1, WalkingZombie zombie2) {
            return Double.compare(zombie2.brainCount, zombie1.brainCount);
        }
    }
}
