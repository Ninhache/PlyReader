package com.example.viewer.types;



import com.example.viewer.stages.util.Maths;
import javafx.beans.property.SimpleDoubleProperty;

public class SimpleNormalizedProperty extends SimpleDoubleProperty {
    private final double MAXIMUM;

    public SimpleNormalizedProperty() {
        this(0, 1);
    }

    public SimpleNormalizedProperty(double initialValue)
    {
        this(initialValue, 1);
    }

    public SimpleNormalizedProperty(double initialValue, double maximum)
    {
        super();
        MAXIMUM = maximum;
        set(initialValue);
    }

    @Override
    public double get() {
        return Maths.clamp(super.get(), 0, MAXIMUM);
    }

    @Override
    public void set(double newValue) {
        super.set(Maths.clamp(newValue, 0, MAXIMUM));
    }

    public void setNormalized(double value) {
        setValue(value * MAXIMUM);
    }

    @Override
    public Double getValue() {
        return get();
    }

    public double getNormalized() {
        return getValue() / MAXIMUM;
    }

    @Override
    public void setValue(Number v) {
        set(v.doubleValue());
    }

    public double getMaximum() {
        return MAXIMUM;
    }
}
