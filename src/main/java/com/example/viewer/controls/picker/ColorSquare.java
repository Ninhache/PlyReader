package com.example.viewer.controls.picker;


import com.example.viewer.ExtendedColor;
import com.example.viewer.stages.util.Assets;
import com.example.viewer.stages.util.ColorUtil;
import com.example.viewer.stages.util.Maths;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class ColorSquare extends ColorSquareAbstract {
    private Pane saturationOverlay;
    private Pane brightnessOverlay;
    private Region colorSquareIndicator;

    private Pane colorBar;
    private Region colorBarIndicator;

    private Region border;

    final double COLOR_BAR_SIZE = 32;
    final double CURSOR_SIZE = 10;
    final double HALF_CURSOR_SIZE = CURSOR_SIZE / 2.0;
    final Insets INSETS = new Insets(HALF_CURSOR_SIZE);

    public ColorSquare() {
        this(Color.RED);
    }

    public ColorSquare(Color color) {
        this(new ExtendedColor(color));
    }

    public ColorSquare(ExtendedColor color) {
        super(color);

        System.out.println(Assets.getAssetPath(""));
        getStylesheets().add(Assets.getAssetPath("css/color-components.css"));
        getStylesheets().add(Assets.getAssetPath("css/color-picker.css"));
        getStyleClass().add("color-picker");

        setSpacing(HALF_CURSOR_SIZE);
        setPadding(new Insets(CURSOR_SIZE));

        // Carré de couleur
        saturationOverlay = new Pane();
        brightnessOverlay = new Pane();

        brightnessOverlay.setBackground(getBrightnessBackground());
        brightnessOverlay.setPadding(INSETS);

        saturationOverlay.getChildren().add(brightnessOverlay);

        colorSquareIndicator = new Region();
        colorSquareIndicator.setCache(true);
        colorSquareIndicator.getStyleClass().add("color-indicator");
        colorSquareIndicator.setPrefSize(CURSOR_SIZE, CURSOR_SIZE);
        colorSquareIndicator.setVisible(true);

        border = new Region();
        border.setCache(true);
        border.getStyleClass().add("color-pane-border");

        brightnessOverlay.getChildren().setAll(border, colorSquareIndicator);

        // Barre de sélection latérale
        colorBar = new Pane();
        colorBar.setBackground(getHueBackground());
        colorBar.setPrefWidth(COLOR_BAR_SIZE + CURSOR_SIZE);

        colorBarIndicator = new Region();
        colorBarIndicator.setCache(true);
        colorBarIndicator.getStyleClass().add("color-indicator");

        colorBarIndicator.setPrefHeight(CURSOR_SIZE);

        colorBar.getChildren().setAll(colorBarIndicator);

        HBox.setHgrow(saturationOverlay, Priority.ALWAYS);
        VBox.setVgrow(saturationOverlay, Priority.ALWAYS);

        VBox.setVgrow(colorBar, Priority.ALWAYS);

        redraw();

        getChildren().addAll(saturationOverlay, colorBar);

        colorBar.setOnMousePressed(this::onBarSelected);
        colorBar.setOnMouseDragged(this::onBarSelected);
        colorBar.setOnScroll(this::onBarScroll);

        brightnessOverlay.setOnMousePressed(this::onSquareSelected);
        brightnessOverlay.setOnMouseDragged(this::onSquareSelected);

        setBindings();
    }

    private void setBindings() {
        // === Taille de la zone carrée de luminosité ===
        brightnessOverlay.prefWidthProperty().bind(saturationOverlay.widthProperty());
        brightnessOverlay.prefHeightProperty().bind(saturationOverlay.heightProperty());

        border.prefWidthProperty().bind(brightnessOverlay.widthProperty().subtract(CURSOR_SIZE).add(2.0));
        border.prefHeightProperty().bind(brightnessOverlay.heightProperty().subtract(CURSOR_SIZE).add(2.0));
        border.setLayoutX(HALF_CURSOR_SIZE - 1.0);
        border.setLayoutY(HALF_CURSOR_SIZE - 1.0);


        //HBox.setHgrow(border, Priority.ALWAYS);
        //VBox.setVgrow(border, Priority.ALWAYS);

        DoubleExpression hueProp = hueProperty().divide(360.0);
        DoubleExpression satProp = saturationProperty();
        DoubleExpression brightProp = brightnessProperty();

        // === Curseur de teinte (verticalement) ===
        // Position verticale
        DoubleExpression barHProp = colorBar.heightProperty().subtract(CURSOR_SIZE);
        colorBarIndicator.layoutYProperty().bind(hueProp.multiply(barHProp));
        // Largeur
        colorBarIndicator.prefWidthProperty().bind(colorBar.widthProperty());

        // === Pointeur de saturation/luminosité (sur le carré) ===
        // Horizontal
        DoubleExpression squareWProp = brightnessOverlay.widthProperty().subtract(CURSOR_SIZE);
        colorSquareIndicator.layoutXProperty().bind(satProp.multiply(squareWProp));
        // Vertical
        DoubleExpression squareHProp = brightnessOverlay.heightProperty().subtract(CURSOR_SIZE);
        colorSquareIndicator.layoutYProperty().bind(Bindings.subtract(1, brightProp).multiply(squareHProp));

        colorBarIndicator.layoutYProperty().addListener(e -> redraw());
    }

    private Background getBrightnessBackground() {
        Stop begin = new Stop(0, new Color(0.0, 0.0, 0.0, 0.0));
        Stop end = new Stop(1, Color.BLACK);

        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.REPEAT, begin, end);
        BackgroundFill fill = new BackgroundFill(gradient, CornerRadii.EMPTY, INSETS);

        return new Background(fill);
    }

    private Background getSaturationBackground(double hue) {
        Color hueColor = Color.hsb(hue, 1.0, 1.0);
        Color hueColorTransparent = new Color(hueColor.getRed(), hueColor.getGreen(), hueColor.getBlue(), 0.0);

        Stop begin = new Stop(0, hueColorTransparent);
        Stop end = new Stop(1, hueColor);

        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.REPEAT, begin, end);
        BackgroundFill fill = new BackgroundFill(gradient, CornerRadii.EMPTY, INSETS);

        return new Background(fill);
    }

    private Background getHueBackground() {
        Stop[] stops = new Stop[360];

        for (int i = 0; i < 360; i++) {
            stops[i] = new Stop(i / 360.0, Color.hsb(i, 1.0, 1.0));
        }

        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.REPEAT, stops);
        BackgroundFill fill = new BackgroundFill(gradient, CornerRadii.EMPTY, INSETS);

        return new Background(fill);
    }

    @Override
    protected void redraw() {
        double hue = 0;

        try {
            hue = hueProperty().get();
        } catch (NullPointerException e) {
            hue = 0;
        }

        if (saturationOverlay != null) {
            saturationOverlay.setBackground(getSaturationBackground(hue));
        }

        Color color = Color.hsb(hue, 1.0, 1.0);

        String hex = ColorUtil.tohexCode(color);

        if (colorBarIndicator != null) {
            colorBarIndicator.setStyle("-fx-border-color: " + hex + ";");
        }
    }


    protected void onBarSelected(MouseEvent e) {
        if (e.getButton() != MouseButton.PRIMARY) return;

        double h = colorBar.getHeight();

        // LERP : Linear intERPolation
        double lerp = Maths.clamp(e.getY(), HALF_CURSOR_SIZE, h - HALF_CURSOR_SIZE - 1) - HALF_CURSOR_SIZE;
        lerp = lerp / (h - CURSOR_SIZE);

        hueProperty().set(lerp * 360.0);
    }

    final double SCROLL_FACTOR = 0.05 * 360.0;

    private void onBarScroll(ScrollEvent e) {
        double hue = hueProperty().get() + 360.0;
        hue -= SCROLL_FACTOR * (e.getDeltaY() / e.getMultiplierY());

        //hue = Maths.clamp(hue, 0.0, 360.0);
        hue = hue % 360.0;

        hueProperty().set(hue);
    }

    private void onSquareSelected(MouseEvent e) {
        if (e.getButton() != MouseButton.PRIMARY) return;

        double w = brightnessOverlay.getWidth();
        double h = brightnessOverlay.getHeight();

        // LERP : Linear intERPolation
        double lerp;

        lerp = Maths.clamp(e.getX(), HALF_CURSOR_SIZE, w - HALF_CURSOR_SIZE) - HALF_CURSOR_SIZE;
        lerp = lerp / (w - CURSOR_SIZE);

        saturationProperty().set(lerp);

        lerp = Maths.clamp(e.getY(), HALF_CURSOR_SIZE, h - HALF_CURSOR_SIZE) - HALF_CURSOR_SIZE;
        lerp = 1 - (lerp / (h - CURSOR_SIZE));

        brightnessProperty().set(lerp);
    }
}
