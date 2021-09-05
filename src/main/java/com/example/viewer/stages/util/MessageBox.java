package com.example.viewer.stages.util;


import com.example.viewer.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/** Affichage de boîtes de dialogue **/
public final class MessageBox {

    public static ButtonType showConfirm(String title, String content) {
        return createAlert(title, content, Alert.AlertType.CONFIRMATION)
                .showAndWait()
                .get();
    }

    public static ButtonType showInfo(String title, String content) {
        return createAlert(title, content, Alert.AlertType.INFORMATION)
                .showAndWait()
                .get();
    }

    public static ButtonType showWarning(String title, String content) {
        return createAlert(title, content, Alert.AlertType.WARNING)
                .showAndWait()
                .get();
    }

    public static ButtonType showError(String title, String content) {
        return createAlert(title, content, Alert.AlertType.ERROR)
                .showAndWait()
                .get();
    }

    public static ButtonType showDoge(String title, String content) {
        Alert alert = createAlert(title, content, Alert.AlertType.ERROR);

        var img = Constants.loadImage(Constants.APP_ICON_ERROR);

        img.setFitWidth(64);
        img.setFitHeight(64);

        alert.setGraphic(img);

        return alert.showAndWait().get();
    }

    private static Alert createAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);

        alert.setTitle("");
        alert.setHeaderText(title);
        alert.setContentText(content);

        return alert;
    }
}
