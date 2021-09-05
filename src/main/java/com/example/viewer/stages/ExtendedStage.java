package com.example.viewer.stages;

import com.example.viewer.Constants;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public abstract class ExtendedStage extends Stage {

    //public final static Image APP_ICON = ;


    public ExtendedStage() {
        getIcons().add(Constants.fileToImage("src/main/resources/icones/handidoge.png"));
    }

    public Tooltip setTooltip(Node node, String text) {
        Tooltip tooltip = new Tooltip(text);
        Tooltip.install(node, tooltip);
        return tooltip;
    }

}
