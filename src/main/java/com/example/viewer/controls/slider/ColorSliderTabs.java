package com.example.viewer.controls.slider;


import com.example.viewer.ExtendedColor;
import com.example.viewer.enums.ColorSpace;
import com.example.viewer.stages.util.Assets;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;
import javafx.scene.paint.Color;


public class ColorSliderTabs extends TabPane {
    private final ExtendedColor COLOR;

    public ColorSliderTabs() {
        this(Color.RED);
    }

    public ColorSliderTabs(Color color) {
        this(new ExtendedColor(color));
    }

    public ColorSliderTabs(ExtendedColor color) {
        getStylesheets().add(Assets.getAssetPath("css/color-tabs.css"));
        this.COLOR = color;

        for (ColorSpace cSpace : ColorSpace.values()) {
            if (cSpace == ColorSpace.HSB) continue;

            ColorSpaceSliderTab tab = new ColorSpaceSliderTab(color, cSpace);

            getTabs().add(tab);
        }

        color.refresh();
    }

    public SimpleObjectProperty<Color> colorProperty() {
        return COLOR.colorProperty();
    }
}