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

public class Field {
    private final List<WalkingZombie> walkingZombie = new ArrayList<>();
    private final Rectangle rectangle;
    private final double x;
    private final double y;

    public Field(double x, double y) throws FileNotFoundException {
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(
                Main.class.getResource("images/field.png")).toString(),
                250, 250, false, false));
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);

        this.x = x;
        this.y = y;

        Label nameOfMacroObject = new Label("Поле");
        nameOfMacroObject.setLayoutX(x + 90);
        nameOfMacroObject.setLayoutY(y - 20);
        nameOfMacroObject.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        nameOfMacroObject.setAlignment(Pos.TOP_CENTER);
        nameOfMacroObject.setTextFill(Color.GOLD);

        rectangle = new Rectangle(x, y - 45, 250, 325);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(2);

        UniversalObject.getInstance().getRoot().getChildren().addAll(imageView, nameOfMacroObject, rectangle);
    }

    public List<WalkingZombie> getWalkingZombie() {
        return walkingZombie;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
