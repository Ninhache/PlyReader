package com.example.viewer.controls;

import com.example.viewer.ExtendedColor;
import com.example.viewer.stages.util.ColorUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ColorThumbnail extends Pane {
    protected final Canvas canvas;
    protected final GraphicsContext graphics;

    private ExtendedColor color;

    public ColorThumbnail() {
        this(Color.RED);
    }

    public ColorThumbnail(Color color) {
        this(new ExtendedColor(color));
    }

    public ColorThumbnail(ExtendedColor color) {
        this.color = color;

        canvas = new Canvas();
        graphics = canvas.getGraphicsContext2D();

        // Liaison de la taille du canvas à celle du conteneur parent
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());

        widthProperty().addListener(e -> draw());
        heightProperty().addListener(e -> draw());
        colorProperty().addListener(e -> draw());

        getChildren().add(canvas);
    }

    public void draw() {
        final double halfWidth = getWidth() / 2;

        Color c = colorProperty().get();

        graphics.setFill(c);
        graphics.fillRect(0,0,halfWidth, getHeight());

        graphics.setFill(ColorUtil.grayScaleColor(c));
        graphics.fillRect(halfWidth,0,halfWidth, getHeight());

        graphics.setLineWidth(1);
        graphics.setStroke(Color.BLACK);
        graphics.strokeRect(0, 0, getWidth(), getHeight());

        graphics.setStroke(Color.WHITE);
        graphics.strokeRect(1, 1, getWidth() - 2, getHeight() - 2);

        graphics.setStroke(Color.BLACK);
        graphics.strokeRect(2, 2, getWidth() - 4, getHeight() - 4);
    }

    @Override
    /** Autorise le redimentionnement de ce widget **/
    public boolean isResizable() {
        return true;
    }

    public SimpleObjectProperty<Color> colorProperty() {
        return color.colorProperty();
    }
}
