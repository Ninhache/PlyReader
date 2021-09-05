package com.example.viewer.controls.picker;



import com.example.viewer.ExtendedColor;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public abstract class ColorSquareAbstract extends HBox {
    // Propriétés
    private boolean localChange = false; // Sert à ne pas créer de boucle de déclenchement d'event infinie
    private ExtendedColor color;

    private SimpleDoubleProperty hueProp = new SimpleDoubleProperty(0);
    private SimpleDoubleProperty satProp = new SimpleDoubleProperty(0);
    private SimpleDoubleProperty brightProp = new SimpleDoubleProperty(0);

    protected ColorSquareAbstract(ExtendedColor color) {
        this.color = color;

        colorProperty().addListener(this::onColorChanged);
        hueProperty().addListener(this::onHueChanged);
        saturationProperty().addListener(this::onSaturationChanged);
        brightnessProperty().addListener(this::onBrightnessChanged);

        colorProperty().set(Color.RED);
    }

    private void onColorChanged(ObservableValue<? extends Color> observable, Color before, Color after) {
        if (!localChange) {
            localChange = true;

            hueProperty().set(after.getHue());
            saturationProperty().set(after.getSaturation());
            brightnessProperty().set(after.getBrightness());

            localChange = false;
        }

        redraw();
    }

    private void onHueChanged(ObservableValue<? extends Number> observable, Number before, Number after) {
        if (!localChange) {
            localChange = true;

            colorProperty().set(Color.hsb(after.doubleValue(), saturationProperty().get(), brightnessProperty().get()));

            localChange = false;
        }

        redraw();
    }

    private void onSaturationChanged(ObservableValue<? extends Number> observable, Number before, Number after) {
        if (!localChange) {
            localChange = true;

            colorProperty().set(Color.hsb(hueProperty().get(), after.doubleValue(), brightnessProperty().get()));

            localChange = false;
        }

        redraw();
    }

    private void onBrightnessChanged(ObservableValue<? extends Number> observable, Number before, Number after) {
        if (!localChange) {
            localChange = true;

            colorProperty().set(Color.hsb(hueProperty().get(), saturationProperty().get(), after.doubleValue()));

            localChange = false;
        }

        redraw();
    }

    public SimpleObjectProperty<Color> colorProperty() {
        return color.colorProperty();
    }

    public SimpleDoubleProperty hueProperty() {
        return color.hueProperty();
    }

    public SimpleDoubleProperty saturationProperty() {
        return color.saturationProperty();
    }

    public SimpleDoubleProperty brightnessProperty() {
        return color.brightnessProperty();
    }

    protected abstract void redraw();
}
