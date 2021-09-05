package com.example.viewer.controls.slider;

import com.example.viewer.ExtendedColor;
import com.example.viewer.enums.ColorComponent;
import com.example.viewer.enums.ColorSpace;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ColorSpaceSliderTab extends Tab {
    private final ColorSpace COLOR_SPACE;
    private final ExtendedColor COLOR;

    public ColorSpaceSliderTab(ColorSpace cSpace) {
        this(Color.RED, cSpace);
    }

    public ColorSpaceSliderTab(Color color, ColorSpace cSpace) {
        this(new ExtendedColor(color), cSpace);
    }


    public ColorSpaceSliderTab(ExtendedColor color, ColorSpace cSpace) {
        super();
        COLOR_SPACE = cSpace;
        COLOR = color;

        setText(cSpace.name());
        setContent(initializeTab());
        setClosable(false);
    }

    private Node initializeTab() {
        VBox page = new VBox();

        page.setPadding(new Insets(12));
        page.setSpacing(12);
        page.setAlignment(Pos.TOP_CENTER);

        for (ColorComponent cComponent : COLOR_SPACE) {
            ColorSlider slider = new ColorSlider(cComponent);
            HBox.setHgrow(slider, Priority.ALWAYS);

            setBindings(slider);

            page.getChildren().add(slider);
        }

        ScrollPane scroll = new ScrollPane(page);

        scroll.getStyleClass().add("tab-content");

        page.prefWidthProperty().bind(scroll.widthProperty());
        scroll.prefHeightProperty().bind(page.prefHeightProperty());

        return scroll;
    }

    private void setBindings(ColorSlider slider) {
        ColorComponent c = slider.getComponent();

        var slideProp = slider.valueProperty();
        var colorProp = c.value01Property(COLOR);

        slideProp.bindBidirectional(colorProp);
    }

    public SimpleObjectProperty<Color> colorProperty() {
        return COLOR.colorProperty();
    }
}
