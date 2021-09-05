package com.example.viewer.controls;


import com.example.viewer.stages.util.Assets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Paint;

/** Scène custom à laquelle est appliqué le fichier de style automatiquement **/
public class StyledScene extends Scene {
    public StyledScene(Parent root) {
        super(root);
        setStyleSheet();
    }

    public StyledScene(Parent root, double width, double height) {
        super(root, width, height);
        setStyleSheet();
    }

    public StyledScene(Parent root, Paint fill) {
        super(root, fill);
        setStyleSheet();
    }

    public StyledScene(Parent root, double width, double height, Paint fill) {
        super(root, width, height, fill);
        setStyleSheet();
    }

    public StyledScene(Parent root, double width, double height, boolean depthBuffer) {
        super(root, width, height, depthBuffer);
        setStyleSheet();
    }

    public StyledScene(Parent root, double width, double height, boolean depthBuffer, SceneAntialiasing antiAliasing) {
        super(root, width, height, depthBuffer, antiAliasing);
        setStyleSheet();
    }

    protected void setStyleSheet() {
        getStylesheets().add(Assets.getAssetPath("/css/style.css"));
    }
}
