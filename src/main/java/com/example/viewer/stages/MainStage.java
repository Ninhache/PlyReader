package com.example.viewer.stages;

import com.example.viewer.*;
import com.example.viewer.controls.StyledScene;
import com.example.viewer.stages.dialogs.ColorChooserDialog;
import com.example.viewer.stages.util.Maths;
import com.example.viewer.stages.util.MessageBox;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MainStage extends ExtendedStage {

    private static final int WIDTH = 1900;
    private static final int HEIGHT = 1000;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    private final Button BUTTON_OPEN, BUTTON_IMPORT, BUTTON_SWITCH, BUTTON_FACE, BUTTON_TRAIT, BUTTON_LIGHT;

    private final FileChooser fileChooser;

    // 3D STUFF
    //final SmartGroup group = new SmartGroup();
    final static Canvas canvas = new Canvas(WIDTH, HEIGHT);

    private static Modele modele;

    public MainStage() throws FileNotFoundException {
        super();

        // WIDGETS
        BorderPane root = new BorderPane();
        ToolBar menu = new ToolBar();

        // BUTTONS
        BUTTON_OPEN = new Button("ColorPicker");
        //BUTTON_OPEN.setDisable(true);
        BUTTON_IMPORT = new Button("Import");
        BUTTON_SWITCH = new Button("Switch");
        BUTTON_FACE = new Button("Face");
        BUTTON_TRAIT = new Button("Trait");
        BUTTON_LIGHT = new Button("Light");
        //BUTTON_LIGHT.setDisable(true);


        //EVENTS
        BUTTON_OPEN.setOnAction(this::onOpenClicked);
        BUTTON_IMPORT.setOnAction(this::onImportClicked);
        BUTTON_SWITCH.setOnAction(this::onSwitchClicked);
        BUTTON_FACE.setOnAction(this::onFacesClicked);
        BUTTON_TRAIT.setOnAction(this::onTraitClicked);
        BUTTON_LIGHT.setOnAction(this::onLightClicked);

        canvas.addEventHandler(MouseEvent.ANY, mouseDraggedEvent());

        //SET
        menu.getItems().addAll(BUTTON_OPEN, BUTTON_IMPORT, BUTTON_SWITCH, BUTTON_FACE, BUTTON_TRAIT, BUTTON_LIGHT);
        root.setTop(menu);
        root.setCenter(canvas);

        // SCENE
        Scene scene = new StyledScene(root,WIDTH, HEIGHT, true);
        setScene(scene);
        setTitle(Constants.APP_NAME);
        scene.setFill(Color.SILVER);

        //initMouseControl(group, scene, this);



        PlyReader pl = new PlyReader();
        // octahedron | apple
        String file = "src/main/resources/modeles/octahedron.ply";


        if(pl.loadPly(file)) modele = pl.getModeleFromPly();

        modele.firstDraw(canvas);
        modele.setTraitDessine(true);
        System.out.println(">>>>>> TRAIT:" + modele.isTraitDessine() + " - FACE:" + modele.isFaceDessine() + " - LUMIERE:" + modele.isLumiereActive());









        //FILE CHOOSER
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(Constants.FILTERS);



        /*
        // KEYCODE COMBINATION

        Runnable kcImport = ()-> {onImportClicked(new ActionEvent());};
        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN), kcImport);
        */


    }

    private void initMouseControl(SmartGroup group, Scene scene, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ() + delta);
        });
    }

    private void onOpenClicked(ActionEvent e) {
        modele.setColor(ColorChooserDialog.open());
        update();
    }

    private void onFacesClicked(ActionEvent e) {
        if(!modele.isFaceDessine()){
            modele.setFaceDessine(true);
        } else {
            modele.setFaceDessine(false);
        }
        update();
    }

    public void onTraitClicked(ActionEvent e) {
        System.out.println("LES TRAITS SONT DESSINE :" + modele.isTraitDessine());
        if(!modele.isTraitDessine()){
            modele.setTraitDessine(true);
        } else {
            modele.setTraitDessine(false);
        }
        update();
    }

    public void onLightClicked(ActionEvent e) {
        if (!modele.isLumiereActive()) {
            modele.setLumiereActive(true);
            modele.setFaceDessine(true);
        } else {
            modele.setLumiereActive(false);
        }
        update();
    }


    private void onSwitchClicked(ActionEvent e) {
        update();
    }

    private void onImportClicked(ActionEvent e) {
        System.out.println("IMPORT");
        Boolean importSucces = false;

        File file = fileChooser.showOpenDialog(this);

        ArrayList<Color> list = new ArrayList<>();
        if (file != null && file.exists()) {
            try {
                PlyReader pl = new PlyReader();

                if(pl.loadPly(file.getAbsolutePath())){
                    modele = pl.getModeleFromPly();

                    modele.firstDraw(canvas);
                }



            } catch (Throwable exc) {
                System.out.println(exc);
                MessageBox.showDoge("Erreur d'importation", "Le contenu du fichier n'est pas valide !");
                importSucces = false;
            }
        }
    }

    public static void update() {
        System.out.println("MODELEINFO TRAIT:" + modele.isTraitDessine() + " - FACE:" + modele.isFaceDessine() + " - LUMIERE:" + modele.isLumiereActive());
        if(!modele.isFaceDessine()){
            modele.draw(canvas);
        } else {
            modele.drawFaces(canvas);
        }
    }

    public EventHandler<MouseEvent> mouseDraggedEvent() {
        EventHandler<MouseEvent> res = new EventHandler<MouseEvent>() {
            double dX;
            double dY;
            double rotationX;
            double rotationY;
            boolean isDragged = false;
            boolean dansFenetre = true;

            @Override
            public void handle(MouseEvent mouseDragged) {
                if (!isDragged && mouseDragged.isDragDetect()) {
                    isDragged = true;
                    dX = mouseDragged.getSceneX();
                    dY = mouseDragged.getSceneY();
                }
                if (isDragged && !mouseDragged.isDragDetect())
                    isDragged = false;

                rotationX = (mouseDragged.getSceneX() - dX);
                rotationY = (mouseDragged.getSceneY() - dY);
                if (mouseDragged.getEventType().equals(MouseEvent.MOUSE_EXITED)) {
                    dansFenetre = false;
                }
                if (mouseDragged.getEventType().equals(MouseEvent.MOUSE_ENTERED))
                    dansFenetre = true;

                /**
                 * Clic gauche = rotation X et Y
                 */
                if (dansFenetre && mouseDragged.isPrimaryButtonDown() && !mouseDragged.isSecondaryButtonDown()) {
                    modele.setMatricePoint(modele.getMatricePoint().translation(-canvas.getWidth() / 2, -canvas.getHeight() / 2, 0));
                    modele.setMatricePoint(modele.getMatricePoint().rotation(Maths.Rotation.Y, rotationX));
                    modele.setMatricePoint(modele.getMatricePoint().rotation(Maths.Rotation.X, rotationY));
                    modele.setMatricePoint(modele.getMatricePoint().translation(canvas.getWidth() / 2, canvas.getHeight() / 2, 0));
                }
                /**
                 * Clic droit = rotation Z
                 */
                if (dansFenetre && !mouseDragged.isPrimaryButtonDown() && mouseDragged.isSecondaryButtonDown()) {
                    modele.setMatricePoint(modele.getMatricePoint().translation(-canvas.getWidth() / 2, -canvas.getHeight() / 2, 0));
                    modele.setMatricePoint(modele.getMatricePoint().rotation(Maths.Rotation.Z, rotationX));
                    modele.setMatricePoint(modele.getMatricePoint().translation(canvas.getWidth() / 2, canvas.getHeight() / 2, 0));
                }
                /**
                 *  Deux clic en même temps = translation
                 */
                if (dansFenetre && mouseDragged.isPrimaryButtonDown() && mouseDragged.isSecondaryButtonDown()) {
                    modele.setMatricePoint(modele.getMatricePoint().translation(-canvas.getWidth() / 2, -canvas.getHeight() / 2, 0));
                    modele.setMatricePoint(modele.getMatricePoint().translation(mouseDragged.getSceneX() - dX, mouseDragged.getSceneY() - dY, 0));
                    modele.setMatricePoint(modele.getMatricePoint().translation(canvas.getWidth() / 2, canvas.getHeight() / 2, 0));
                }
                dX = mouseDragged.getSceneX();
                dY = mouseDragged.getSceneY();
            }
        };
        return res;
    }

}
