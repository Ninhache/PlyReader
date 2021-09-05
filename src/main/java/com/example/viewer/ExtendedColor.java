package com.example.viewer;

import com.example.viewer.types.SimpleNormalizedProperty;
import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

/**
 * Couleur étendue supportant l'espace de couleur CMJK (Cyan Magenta Jaune)
 * L'ajout de cet espace de couleur se justifie par l'emploi du logiciel:
 * rendre les couleurs IMPRIMABLES correctement en noir et blanc
 *
 * Les imprimantes utilisent généralement des cartouches Cyan jaune Magenta et Noires
 * Nous avons donc jugé utile d'indiquer ces valeurs dans la fenêtre du logiciel
 *
 * Conversion RGB <==> CMYK:
 * https://www.rapidtables.com/convert/color/cmyk-to-rgb.html
 * https://www.rapidtables.com/convert/color/rgb-to-cmyk.html
 */
public class ExtendedColor {
    // === ATTRIBUTS ===

    // == BASE ==
    private final SimpleObjectProperty<Color> color = new SimpleObjectProperty<Color>();

    private final SimpleNormalizedProperty alpha = new SimpleNormalizedProperty(0);

    // == RGB ==
    private final SimpleNormalizedProperty red = new SimpleNormalizedProperty(0);
    private final SimpleNormalizedProperty green = new SimpleNormalizedProperty(0);
    private final SimpleNormalizedProperty blue = new SimpleNormalizedProperty(0);

    // == HSB ==
    private final SimpleDoubleProperty hue = new SimpleDoubleProperty(0);
    private final SimpleNormalizedProperty saturation = new SimpleNormalizedProperty(0);
    private final SimpleNormalizedProperty brightness = new SimpleNormalizedProperty(0);

    // == CMYK ==
    private final SimpleNormalizedProperty cyan = new SimpleNormalizedProperty(0);
    private final SimpleNormalizedProperty magenta = new SimpleNormalizedProperty(0);
    private final SimpleNormalizedProperty yellow = new SimpleNormalizedProperty(0);
    private final SimpleNormalizedProperty black = new SimpleNormalizedProperty(0);

    private boolean localChange = false;

    // === CONSTRUCTEURS ===

    public ExtendedColor(Color color) {
        // == BASE ==
        colorProperty().addListener(this::onColorChanged);
        alphaProperty().addListener(this::onChangedRGBA);

        // == RGB ==
        redProperty().addListener(this::onChangedRGBA);
        greenProperty().addListener(this::onChangedRGBA);
        blueProperty().addListener(this::onChangedRGBA);

        // == HSB ==
        hueProperty().addListener(this::onChangedHSBA);
        saturationProperty().addListener(this::onChangedHSBA);
        brightnessProperty().addListener(this::onChangedHSBA);

        // == CMYK ==
        cyanProperty().addListener(this::onChangedCMYKA);
        magentaProperty().addListener(this::onChangedCMYKA);
        yellowProperty().addListener(this::onChangedCMYKA);
        blackProperty().addListener(this::onChangedCMYKA);

        this.color.set(color);
    }

    public ExtendedColor(String color) {
        this(Color.web(color));
    }

    // === PROPRIETES ===

    // == BASE ==
    public SimpleObjectProperty<Color> colorProperty() {
        return color;
    }

    public SimpleNormalizedProperty alphaProperty() {
        return alpha;
    }

    // == RGB ==
    public SimpleNormalizedProperty redProperty() {
        return red;
    }

    public SimpleNormalizedProperty greenProperty() {
        return green;
    }

    public SimpleNormalizedProperty blueProperty() {
        return blue;
    }

    // == HSB ==
    public SimpleDoubleProperty hueProperty() {
        return hue;
    }

    public SimpleNormalizedProperty saturationProperty() {
        return saturation;
    }

    public SimpleNormalizedProperty brightnessProperty() {
        return brightness;
    }

    // == CMYK ==
    public SimpleNormalizedProperty cyanProperty() {
        return cyan;
    }

    public SimpleNormalizedProperty magentaProperty() {
        return magenta;
    }

    public SimpleNormalizedProperty yellowProperty() {
        return yellow;
    }

    public SimpleNormalizedProperty blackProperty() {
        return black;
    }

    // === GETTERS & SETTERS ===

    public double getAlpha() {
        return alpha.get();
    }

    public void setAlpha(double value) {
        alpha.set(value);
    }

    // == BASE ==
    public Color getColor() {
        return color.get();
    }

    public void setColor(Color value) {
        color.set(value);
    }

    // == RGB ==
    public double getRed() {
        return red.get();
    }

    public void setRed(double value) {
        red.set(value);
    }

    public double getGreen() {
        return green.get();
    }

    public void setGreen(double value) {
        green.set(value);
    }

    public double getBlue() {
        return blue.get();
    }

    public void setBlue(double value) {
        blue.set(value);
    }

    // == HSB ==
    public double getHue() {
        return hue.get();
    }

    public void setHue(double value) {
        hue.set(value);
    }

    public double getSaturation() {
        return saturation.get();
    }

    public void setSaturation(double value) {
        saturation.set(value);
    }

    public double getBrightness() {
        return brightness.get();
    }

    public void setBrightness(double value) {
        brightness.set(value);
    }

    // == CMYK ==
    public double getCyan() {
        return cyan.get();
    }

    public void setCyan(double value) {
        cyan.set(value);
    }

    public double getMagenta() {
        return magenta.get();
    }

    public void setMagenta(double value) {
        magenta.set(value);
    }

    public double getYellow() {
        return yellow.get();
    }

    public void setYellow(double value) {
        yellow.set(value);
    }

    public double getBlack() {
        return black.get();
    }

    public void setBlack(double value) {
        black.set(value);
    }

    // === EVENT LISTENERS ===

    // == BASE ==

    protected void onColorChanged(Observable observable) {
        if (!localChange) {
            localChange = true;

            final double r = getColor().getRed();
            final double g = getColor().getGreen();
            final double b = getColor().getBlue();

            setRed(r);
            setGreen(g);
            setBlue(b);

            setSaturation(getColor().getSaturation());
            setBrightness(getColor().getBrightness());
            setHue(getColor().getHue());

            // == CMYK ==

            final double k = 1 - Math.max(Math.max(getRed(), getGreen()),getBlue());

            //if (k < 1) {
            final double c = zeroIfNaN((1 - r - k) / (1 - k));
            final double m = zeroIfNaN((1 - g - k) / (1 - k));
            final double y = zeroIfNaN((1 - b - k) / (1 - k));

            setBlack(k);
            setCyan(c);
            setMagenta(m);
            setYellow(y);
            //} else {
            //    setBlack(1);
            //}

            localChange = false;
        }
    }

    private double zeroIfNaN(double value) {
        return Double.isNaN(value) ? 0 : value;
    }

    public void refresh() {
        onColorChanged(colorProperty());
    }

    protected void onChangedRGBA(Observable observable) {
        if (!localChange) {
            final double r = getRed();
            final double g = getGreen();
            final double b = getBlue();

            final double a = getAlpha();

            setColor(new Color(r, g, b, a));
        }
    }

    protected void onChangedHSBA(Observable observable) {
        if (!localChange) {
            final double h = getHue();
            final double s = getSaturation();
            final double b = getBrightness();

            final double a = getAlpha();

            setColor(Color.hsb(h, s, b, a));
        }
    }

    protected void onChangedCMYKA(Observable observable) {
        if (!localChange) {
            final double k = zeroIfNaN(getBlack());
            final double c = zeroIfNaN(getCyan());
            final double m = zeroIfNaN(getMagenta());
            final double y = zeroIfNaN(getYellow());

            final double r = (1 - c) * (1 - k);
            final double g = (1 - m) * (1 - k);
            final double b = (1 - y) * (1 - k);

            final double a = getAlpha();

            setColor(new Color(r, g, b, a));
        }
    }
}