package com.example.viewer.enums;


import com.example.viewer.ExtendedColor;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;

import java.util.function.Function;

public enum ColorComponent {
    RED        ("Rouge",      Color.web("de6060"), ExtendedColor::redProperty,        255),
    GREEN      ("Vert",       Color.web("a3cf4a"), ExtendedColor::greenProperty,      255),
    BLUE       ("Bleu",       Color.web("60a7de"), ExtendedColor::blueProperty,       255),
    HUE        ("Teinte",     Color.web("ffffff"), ExtendedColor::hueProperty,        360),
    SATURATION ("Saturation", Color.web("ffffff"), ExtendedColor::saturationProperty, 100),
    BRIGHTNESS ("Luminosité", Color.web("ffffff"), ExtendedColor::brightnessProperty, 100),
    CYAN       ("Cyan",       Color.web("14d2d9"), ExtendedColor::cyanProperty,       100),
    MAGENTA    ("Magenta",    Color.web("d94acd"), ExtendedColor::magentaProperty,    100),
    YELLOW     ("Jaune",      Color.web("d9c345"), ExtendedColor::yellowProperty,     100),
    BLACK      ("Noir",       Color.web("424242"), ExtendedColor::blackProperty,      100);

    private final Color HEADER_COLOR;
    private final String HEADER_NAME;
    private final Function<ExtendedColor, SimpleDoubleProperty> GETTER;
    private final int MAXIMUM_VALUE;

    private ColorComponent(String headerName, Color headerColor, Function<ExtendedColor, SimpleDoubleProperty> componentGetter, int max) {
        HEADER_NAME = headerName;
        HEADER_COLOR = headerColor;
        GETTER = componentGetter;
        MAXIMUM_VALUE = max;
    }

    private ColorComponent(String headerName, Color headerColor, Function<ExtendedColor, SimpleDoubleProperty> componentGetter) {
        this(headerName, headerColor, componentGetter, 255);
    }

    public double getValue01(ExtendedColor color) {
        return GETTER.apply(color).get();
    }

    public double getValue01(Color color) {
        return getValue01(new ExtendedColor(color));
    }

    public int getValueInt(ExtendedColor color) {
        final int MAXIMUM = equals(HUE) ? 1 : MAXIMUM_VALUE;
        return (int)Math.round(getValue01(color) * MAXIMUM);
    }

    public int getValueInt(Color color) {
        return getValueInt(new ExtendedColor(color));
    }

    public int getMaximum() {
        return MAXIMUM_VALUE;
    }

    public String cssClass() {
        return name().toLowerCase();
    }

    public Color getHeaderColor() {
        return HEADER_COLOR;
    }

    public SimpleDoubleProperty value01Property(ExtendedColor color) {
        return GETTER.apply(color);
    }

    public SimpleDoubleProperty valueProperty(ExtendedColor color) {
        return (SimpleDoubleProperty)Bindings.multiply(MAXIMUM_VALUE, GETTER.apply(color));
    }

    @Override
    public String toString() {
        return HEADER_NAME;
    }
}
