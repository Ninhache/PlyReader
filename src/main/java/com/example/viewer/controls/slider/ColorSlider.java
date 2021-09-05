package com.example.viewer.controls.slider;

import com.example.viewer.enums.ColorComponent;
import com.example.viewer.stages.util.Assets;
import com.example.viewer.stages.util.Maths;
import com.example.viewer.types.SimpleNormalizedProperty;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ColorSlider extends HBox {
    private SimpleObjectProperty<ColorComponent> component = new SimpleObjectProperty<ColorComponent>();
    private SimpleNormalizedProperty value;

    private final Label name;
    private final Slider slider;
    private final Spinner<Integer> spinner;
    private SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory;

    private boolean localChange = false;

    public ColorSlider(ColorComponent component) {
        value = new SimpleNormalizedProperty(0, component.equals(ColorComponent.HUE) ? 360 : 1);


        getStylesheets().add(Assets.getAssetPath("css/color-sliders.css"));
        getStylesheets().add(Assets.getAssetPath("css/color-components.css"));

        name = new Label();
        name.setPrefWidth(60);
        name.minWidthProperty().bind(name.prefWidthProperty());
        name.setAlignment(Pos.CENTER_RIGHT);

        slider = new Slider();
        slider.setSnapToTicks(false);
        slider.setShowTickMarks(false);
        slider.setShowTickLabels(false);
        slider.getStyleClass().add("color-slider");

        HBox.setHgrow(slider, Priority.ALWAYS);

        spinner = new Spinner<Integer>();
        spinner.setPrefWidth(70);
        spinner.minWidthProperty().bind(spinner.prefWidthProperty());

        this.component.addListener(this::onComponentChanged);
        this.component.set(component);

        setPadding(new Insets(4));
        setSpacing(12);
        setAlignment(Pos.CENTER);

        getChildren().addAll(name, slider, spinner);

        slider.valueProperty().addListener(this::onSliderChanged);
        spinner.valueProperty().addListener(this::onSpinnerChanged);

        value.addListener(this::onValueChanged);
    }

    private void onValueChanged(Observable observable) {
        //String s = String.format("%s\t = %f", getComponent().name(), value.get());
        //System.out.println(s);

        if (!localChange) {
            slider.setValue(value.getNormalized() * 100.0);
        }
    }

    private void onSliderChanged(Observable observable) {
        String s = String.format("%s = %s", component.get().name(), Double.toString(slider.getValue()));
        //System.out.println(s);

        if (!localChange) {
            localChange = true;

            final int maximum = component.get().getMaximum();
            int newValue = (int)(slider.getValue() / 100.0 * maximum);

            valueFactory.setValue(newValue);

            value.setNormalized(Maths.clamp01(slider.getValue() / 100.0));

            localChange = false;
        }
    }

    private void onSpinnerChanged(Observable observable) {
        if (!localChange) {
            localChange = true;

            final double maximum = component.get().getMaximum();
            final double newValue = (double)spinner.getValue() / maximum;

            slider.setValue(newValue * 100.0);
            value.setNormalized(newValue);

            localChange = false;
        }
    }

    private void onComponentChanged(ObservableValue<? extends ColorComponent> observable, ColorComponent oldValue, ColorComponent newValue) {
        name.setText(component.get().toString());

        if (newValue != null) {
            slider.setId(newValue.cssClass());

            valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, newValue.getMaximum(), 0, 1);
            spinner.setValueFactory(valueFactory);
        }
    }

    public SimpleObjectProperty<ColorComponent> componentProperty() {
        return component;
    }

    public ColorComponent getComponent() {
        return component.get();
    }

    public void setComponent(ColorComponent value) {
        component.set(value);
    }

    public DoubleProperty valueProperty() {
        return value;
    }

    public double getValue() {
        return value.get();
    }

    public void setValue(double value) {
        this.value.set(value);
    }
}
