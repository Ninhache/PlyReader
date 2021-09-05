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
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

public class MainStage extends ExtendedStage {

    private static final int WIDTH = 1900;
    private static final int HEIGHT = 1000;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    private final Button BUTTON_OPEN, BUTTON_IMPORT, BUTTON_SWITCH;

    private final FileChooser fileChooser;

    // 3D STUFF
    //final SmartGroup group = new SmartGroup();
    final Canvas canvas = new Canvas(WIDTH, HEIGHT);
    private Modele modele;

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


        //EVENTS
        BUTTON_OPEN.setOnAction(this::onOpenClicked);
        BUTTON_IMPORT.setOnAction(this::onImportClicked);
        BUTTON_SWITCH.setOnAction(this::onSwitchClicked);
        canvas.addEventHandler(MouseEvent.ANY, mouseDraggedEvent());

        //SET
        menu.getItems().addAll(BUTTON_OPEN, BUTTON_IMPORT, BUTTON_SWITCH);
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


        if(pl.loadPly(file)) modele = pl.getPointsFromPly();

        modele.firstDraw(canvas);
        modele.setTraitDessine(true);

        System.out.println(modele.toString());













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
                    modele = pl.getPointsFromPly();

                    modele.firstDraw(canvas);
                }



            } catch (Throwable exc) {
                System.out.println(exc);
                MessageBox.showDoge("Erreur d'importation", "Le contenu du fichier n'est pas valide !");
                importSucces = false;
            }
        }
    }

    public void update() {
        if(modele.isFaceDessine()){
            modele.setFaceDessine(false);
            modele.setTraitDessine(true);
        }else{
            modele.setFaceDessine(true);
            modele.setTraitDessine(false);
        }
        canvasRedraw();
    }

    public void canvasRedraw(){
        if(modele.isFaceDessine()){
            modele.draw(canvas);
        }else{
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
                canvasRedraw();
            }
        };
        return res;
    }

}