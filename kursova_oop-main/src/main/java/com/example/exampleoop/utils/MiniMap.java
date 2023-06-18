package com.example.exampleoop.utils;

import com.example.exampleoop.Main;
import com.example.exampleoop.gameEntities.UniversalObject;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class MiniMap {
    private final double minimapScale = 0.1;
    private final double minimapBaseX = 1040.0;
    private final double minimapBaseY = 5.0;
    private final Stage stage = Main.getStage();
    private final Group group = new Group();
    private boolean imageViewMapFirstTime = true;
    private Rectangle minimapBorderRect;
    private Rectangle activeAreaRect;
    private ImageView imageViewMap;
    private LocalDateTime beginTime = LocalDateTime.now();

    public MiniMap() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                LocalDateTime nextTime = LocalDateTime.now();

                if (ChronoUnit.SECONDS.between(beginTime, nextTime) > 0) {
                    beginTime = nextTime;

                    if (imageViewMapFirstTime) {
                        imageViewMapFirstTime = false;
                        initializeMinimapBorderRect();
                    }

                    try {
                        updateImageViewMap();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        timer.start();
    }

    public void moveTo(double x, double y) {
        Main.getScrollPane().setHvalue((x / minimapBorderRect.getWidth()) / 10);
        Main.getScrollPane().setVvalue((y / minimapBorderRect.getHeight()) / 10);
    }

    private void initializeMinimapBorderRect() {
        double imageWidth = UniversalObject.getImageWidth() * minimapScale;
        double imageHeight = UniversalObject.getImageHeight() * minimapScale;

        minimapBorderRect = new Rectangle(stage.getWidth(), stage.getHeight());
        minimapBorderRect.setFill(Color.TRANSPARENT);
        minimapBorderRect.setStrokeWidth(3);
        minimapBorderRect.setStroke(Color.BLACK);
        minimapBorderRect.setX(minimapBaseX);
        minimapBorderRect.setY(minimapBaseY);
        minimapBorderRect.setWidth(imageWidth);
        minimapBorderRect.setHeight(imageHeight);
        minimapBorderRect.autosize();
        group.getChildren().add(minimapBorderRect);
    }

    public void updateImageViewMap() throws FileNotFoundException {
        WritableImage snapshot = UniversalObject.getInstance().getRoot().snapshot(new SnapshotParameters(), null);

        if (imageViewMap == null) {
            imageViewMap = new ImageView(snapshot);
            group.getChildren().add(imageViewMap);
            Scale scale = new Scale(minimapScale, minimapScale);
            imageViewMap.setLayoutX(minimapBaseX);
            imageViewMap.setLayoutY(minimapBaseY);
            imageViewMap.getTransforms().add(scale);
            imageViewMap.setOnMousePressed(event -> moveTo(event.getX(), event.getY()));
        } else {
            imageViewMap.setImage(snapshot);
        }
    }

    public void updateActiveAreaRect(double xLoc, double yLoc) {
        double activeAreaWidth = Main.getScrollPane().getWidth() * minimapScale;
        double activeAreaHeight = Main.getScrollPane().getHeight() * minimapScale;

        double xPos = xLoc - minimapBaseX / 1000;
        double yPos = yLoc - minimapBaseY / 1000;

        if (activeAreaRect == null) {
            activeAreaRect = new Rectangle(activeAreaWidth, activeAreaHeight);
            activeAreaRect.setFill(Color.TRANSPARENT);
            activeAreaRect.setStrokeWidth(2);
            activeAreaRect.setStroke(Color.YELLOW);
            activeAreaRect.setX(xPos);
            activeAreaRect.setY(yPos);
            group.getChildren().add(activeAreaRect);
        } else {
            Main.getRoot().getChildren().remove(activeAreaRect);
        }

        if (yLoc > 190) yPos -= 70;
        if (xLoc > 1280) xPos -= 130;
        activeAreaRect.setX(xPos);
        activeAreaRect.setY(yPos);

        activeAreaRect.setWidth(activeAreaWidth);
        activeAreaRect.setHeight(activeAreaHeight);
        Main.getRoot().getChildren().add(activeAreaRect);
    }

    public Group getGroup() {
        return group;
    }

    public double getMinimapScale() {
        return minimapScale;
    }
}
