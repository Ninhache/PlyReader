package com.example.viewer;

import com.example.viewer.stages.util.Assets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.MalformedURLException;

public class Constants {

    public static final String APP_NAME = "PlyViewer";
    public static final Image APP_ICON = fileToImage("src/main/resources/icones/handidoge.png");
    public static final Image APP_ICON_ERROR = fileToImage("src/main/resources/icones/handidoge.png");


    public static final FileChooser.ExtensionFilter[] FILTERS = new FileChooser.ExtensionFilter[]{
            new FileChooser.ExtensionFilter("Fichier ply (*.ply)", "*.ply"),
            new FileChooser.ExtensionFilter("Tous les fichiers (*)", "*"),
    };

    public static Image fileToImage(String path) {
        Image i = null;
        try {
            File f = new File(path);
            i = new Image(f.toURI().toURL().toString());
        }catch (Exception e){
            System.out.println("PROBLEME LOAD IMAGE");
        }
        return i;
    }

    public static Image loadImageRes(String path) {
        return new Image(Assets.getAssetPath(path));
    }

    public static final ImageView loadImage(Image src){
        ImageView img = new ImageView(src);
        img.setFitWidth(20);
        img.setFitHeight(20);
        img.setPreserveRatio(false);
        return img;
    }

}
