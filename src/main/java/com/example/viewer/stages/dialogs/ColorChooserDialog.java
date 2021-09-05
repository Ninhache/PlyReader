package com.example.viewer.stages.dialogs;


import com.example.viewer.ExtendedColor;
import com.example.viewer.controls.ColorThumbnail;
import com.example.viewer.controls.StyledScene;
import com.example.viewer.controls.picker.ColorSquare;
import com.example.viewer.controls.slider.ColorSliderTabs;
import com.example.viewer.stages.util.Assets;
import com.example.viewer.stages.ExtendedStage;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

public class ColorChooserDialog extends ExtendedStage {
    private final ExtendedColor color;

    private Color result = null;

    public ColorChooserDialog() {
        this(Color.LIME);
    }

    public ColorChooserDialog(ExtendedColor color) {
        this(color.getColor());
    }

    public ColorChooserDialog(Color color) {
        this.color = new ExtendedColor(color);

        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);

        // INSTANTIATE: Color picker + preview
        ColorSquare square = new ColorSquare(color);
        square.setPrefSize(300, 300);

        ColorThumbnail thumb = new ColorThumbnail(color);
        thumb.setPrefSize(300, 32);

        ColorSliderTabs sliders = new ColorSliderTabs(color);
        sliders.setPrefSize(300, 250);

        // LAYOUT: Color picker + preview

        TabPane colorTabPane = new TabPane();

        HBox thumbPane = new HBox(thumb);
        thumbPane.setPadding(new Insets(0, 12, 12, 12));

        VBox colorRoot = new VBox(square, thumbPane);

        Tab colorTab = new Tab("Couleur", colorRoot);
        colorTab.setClosable(false);
        colorTabPane.getTabs().add(colorTab);

        VBox sliderRoot = new VBox();

        Button confirmButton = new Button("Sélectionner");
        confirmButton.setDefaultButton(true);
        confirmButton.setOnAction(this::onConfirmClicked);
        confirmButton.getStyleClass().addAll("pill-button", "left-pill");

        Button cancelButton = new Button("Annuler");
        cancelButton.setOnAction(this::onCancelClicked);
        cancelButton.getStyleClass().addAll("pill-button", "right-pill");

        HBox buttonsRoot = new HBox(confirmButton, cancelButton);
        buttonsRoot.getStylesheets().add(Assets.getAssetPath("/css/pill-button.css"));
        buttonsRoot.setAlignment(Pos.CENTER_RIGHT);
        buttonsRoot.setPadding(thumbPane.getPadding());

        sliderRoot.getChildren().addAll(sliders, buttonsRoot);

        root.getChildren().addAll(colorTabPane, sliderRoot);

        setGrow(square, Priority.ALWAYS, Priority.ALWAYS);
        setGrow(thumb, Priority.ALWAYS, Priority.ALWAYS);
        setGrow(sliders, Priority.ALWAYS, Priority.ALWAYS);
        setGrow(thumbPane, Priority.ALWAYS, Priority.SOMETIMES);
        setGrow(colorTabPane, Priority.SOMETIMES, Priority.ALWAYS);

        thumb.colorProperty().bind(square.colorProperty());
        sliders.colorProperty().bindBidirectional(square.colorProperty());
        this.color.colorProperty().bind(square.colorProperty());

        StyledScene scene = new StyledScene(root);
        setScene(scene);
        setTitle("Sélecteur de couleur");
        setResizable(false);
        this.initStyle(StageStyle.UTILITY);

        confirmButton.requestFocus();
    }

    private void onConfirmClicked(ActionEvent e) {
        result = getColor();
        close();
    }

    private void onCancelClicked(ActionEvent e) {
        close();
    }

    private void setGrow(Node node, Priority hgrow, Priority vgrow) {
        HBox.setHgrow(node, hgrow);
        VBox.setVgrow(node, vgrow);
    }

    public Color getColor() {
        return color.getColor();
    }

    public static Color open() {
        return open(Color.RED);
    }

    public static Color open(Color color) {
        return open(new ExtendedColor(color));
    }

    public static Color open(ExtendedColor initialColor) {
        ColorChooserDialog dialog = new ColorChooserDialog(initialColor);

        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();

        return dialog.result;
    }
}
