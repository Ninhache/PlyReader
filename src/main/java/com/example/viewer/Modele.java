package com.example.viewer;

import com.example.viewer.stages.MainStage;
import com.example.viewer.stages.util.Maths;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;

import static javafx.scene.paint.Color.BLACK;

public class Modele {

    private ArrayList<Face> listeFace;
    private ArrayList<Point> listePoint;
    private Maths.Matrice matricePoint;
    private double rapport;
    private String author;
    private String description;

    private Point tmpPoint;

    // ????
    public boolean rapportHorizontal;

    // TODO
    private boolean traitDessine;
    private boolean faceDessine;
    private boolean lumiereActive;

    private boolean isTurning; // =>>>
    private Timeline rotationAuto;

    public Modele(int nbPoint){
        this.author = "Auteur inconnu";
        this.description = "Pas de description";

        this.listeFace = new ArrayList<>();
        this.listePoint = new ArrayList<>();

        this.setFaceDessine(false);
        this.setTraitDessine(false);

        //TODO ADD LUMOS
        this.setLumiereActive(false);
    }

    public void initMatrice() {
        double[][] tabPointPourMatrice = new double[4][listePoint.size()];
        for(Point p : listePoint) {
            tabPointPourMatrice[0][p.getId()] = p.getX();
            tabPointPourMatrice[1][p.getId()] = p.getY();
            tabPointPourMatrice[2][p.getId()] = p.getZ();
            tabPointPourMatrice[3][p.getId()] = 1;
        }
        this.matricePoint = new Maths.Matrice(tabPointPourMatrice);
    }

    public void firstDraw(Canvas canvas) {
        final double RAPPORT_MISE_A_L_ECHELLE = 0.60;
        final double MISE_A_L_ECHELLE_HORIZONTALE = canvas.getWidth()/this.rapport*RAPPORT_MISE_A_L_ECHELLE;
        final double MISE_A_L_ECHELLE_VERTICALE = canvas.getHeight()/this.rapport*RAPPORT_MISE_A_L_ECHELLE;
        this.matricePoint = this.matricePoint.translation(-tmpPoint.getX(), -tmpPoint.getY(), -tmpPoint.getZ());
        if(rapportHorizontal) {
            this.matricePoint = this.matricePoint.multiplication( MISE_A_L_ECHELLE_HORIZONTALE);
            this.tmpPoint.setX(tmpPoint.getX() * MISE_A_L_ECHELLE_HORIZONTALE);
            this.tmpPoint.setY(tmpPoint.getY() * MISE_A_L_ECHELLE_HORIZONTALE);
            this.tmpPoint.setZ(tmpPoint.getZ() * MISE_A_L_ECHELLE_HORIZONTALE);
        } else {
            this.matricePoint = this.matricePoint.multiplication(MISE_A_L_ECHELLE_VERTICALE);
            this.tmpPoint.setX(tmpPoint.getX() * MISE_A_L_ECHELLE_VERTICALE);
            this.tmpPoint.setY(tmpPoint.getY() * MISE_A_L_ECHELLE_VERTICALE);
            this.tmpPoint.setZ(tmpPoint.getZ() * MISE_A_L_ECHELLE_VERTICALE);
        }

        this.matricePoint = this.matricePoint.rotation(Maths.Rotation.X, 180);
        this.matricePoint = this.matricePoint.translation(canvas.getWidth()/2, canvas.getHeight()/2, 0);
        draw(canvas);
    }

    public void draw(Canvas canvas)  {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        int pt1, pt2;
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.beginPath();
        gc.setLineWidth(1);
        gc.setStroke(BLACK);
        for (Face face : listeFace) {
            for(int i = 0; i < face.getListePoint().size(); i++) {
                pt1 = face.getListePoint().get(i).getId();
                if(i < face.getListePoint().size() - 1) {
                    pt2 = face.getListePoint().get(i + 1).getId();

                } else {
                    pt2 = face.getListePoint().get(0).getId();
                }
                //gc.strokeLine(0,5,60,800);
                //System.out.printf("%s | %s | %s | %s\n", matricePoint.getM()[0][pt1], matricePoint.getM()[1][pt1], matricePoint.getM()[0][pt2], matricePoint.getM()[1][pt2]);
                gc.strokeLine(matricePoint.getM()[0][pt1], matricePoint.getM()[1][pt1], matricePoint.getM()[0][pt2], matricePoint.getM()[1][pt2]);
            }
        }
        gc.closePath();

        /*GraphicsContext gc = canvas.getGraphicsContext2D();

        int pt1, pt2;
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.beginPath();
        gc.setLineWidth(1);
        gc.setStroke(BLACK);

        for (Face face : listeFace) {
            for(int i = 0; i < face.getListePoint().size(); i++) {
                pt1 = face.getListePoint().get(i).getId();
                if(i < face.getListePoint().size() - 1) {
                    pt2 = face.getListePoint().get(i + 1).getId();
                } else {
                    pt2 = face.getListePoint().get(0).getId();
                }
                gc.strokeLine(matricePoint.getM()[0][pt1], matricePoint.getM()[1][pt1], matricePoint.getM()[0][pt2], matricePoint.getM()[1][pt2]);
            }
        }
        gc.closePath();*/
    }

    public void setColor(Color c){
        for (Face f : this.listeFace) {
            f.setColor(new ExtendedColor(c));
        }
    }

    public void drawFaces(Canvas canvas) {
        double[] coordX;
        double[] coordY;
        double[] coordZ;
        Maths.Vecteur vecteurLumiere = new Maths.Vecteur(0, 0, -1);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.beginPath();
        Collections.sort(listeFace);

        for (Face face : listeFace) {
            System.out.println(face);
            coordX = new double[face.getListePoint().size()];
            coordY = new double[face.getListePoint().size()];
            coordZ = new double[face.getListePoint().size()];
            for(int i = 0; i < face.getListePoint().size(); i++) {
                coordX[i] = matricePoint.getM()[0][face.getListePoint().get(i).getId()];
                face.getListePoint().get(i).setX(coordX[i]);
                coordY[i] = matricePoint.getM()[1][face.getListePoint().get(i).getId()];
                face.getListePoint().get(i).setY(coordY[i]);
                coordZ[i] = matricePoint.getM()[2][face.getListePoint().get(i).getId()];
                face.getListePoint().get(i).setZ(coordZ[i]);
            }
            if(this.traitDessine) {
                gc.strokePolygon(coordX, coordY, coordX.length);
            }
            if(this.lumiereActive) {
                Maths.Vecteur vecteurFace1 = new Maths.Vecteur(coordX[1]-coordX[0], coordY[1]-coordY[0], coordZ[1] - coordZ[0]);
                Maths.Vecteur vecteurFace2 = new Maths.Vecteur(coordX[coordX.length-1]-coordX[0], coordY[coordY.length-1]-coordY[0], coordZ[coordZ.length-1] - coordZ[0]);
                Maths.Vecteur vecteurNormal = vecteurFace1.produitVectoriel(vecteurFace2);
                double coeffLumineux = (Math.cos((vecteurLumiere.Normalisation()).produitScalaire(vecteurNormal.Normalisation())));
                gc.setFill(Color.rgb((int)(face.getColor().getRed()*255*coeffLumineux), (int)(face.getColor().getBlue()*255*coeffLumineux), (int)(face.getColor().getGreen()*255*coeffLumineux)));
            }else {
                gc.setFill(face.getColor().getColor());
            }
            gc.fillPolygon(coordX, coordY, coordX.length);
        }
    }

    public double getMaxX(){
        double tmp = listePoint.get(0).getX();
        for (Point p : listePoint ) {
            if(p.getX() > tmp){
                tmp = p.getX();
            }
        }
        return tmp;
    }

    public double getMinX(){
        double tmp = listePoint.get(0).getX();
        for (Point p : listePoint ) {
            if(p.getX() < tmp){
                tmp = p.getX();
            }
        }
        return tmp;
    }

    public double getMaxY(){
        double tmp = listePoint.get(0).getY();
        for (Point p : listePoint ) {
            if(p.getY() > tmp){
                tmp = p.getY();
            }
        }
        return tmp;
    }

    public double getMinY(){
        double tmp = listePoint.get(0).getY();
        for (Point p : listePoint ) {
            if(p.getX() < tmp){
                tmp = p.getY();
            }
        }
        return tmp;
    }

    public double getMaxZ(){
        double tmp = listePoint.get(0).getZ();
        for (Point p : listePoint ) {
            if(p.getZ() > tmp){
                tmp = p.getZ();
            }
        }
        return tmp;
    }

    public double getMinZ(){
        double tmp = listePoint.get(0).getZ();
        for (Point p : listePoint ) {
            if(p.getX() < tmp){
                tmp = p.getZ();
            }
        }
        return tmp;
    }

    public ArrayList<Face> getListeFace() {
        return listeFace;
    }

    public void setListeFace(ArrayList<Face> listeFace) {
        this.listeFace = listeFace;
    }

    public ArrayList<Point> getListePoint() {
        return listePoint;
    }

    public void setListePoint(ArrayList<Point> listePoint) {
        this.listePoint = listePoint;
    }

    public Maths.Matrice getMatricePoint() {
        return matricePoint;
    }

    public void setMatricePoint(Maths.Matrice matricePoint) {
        this.matricePoint = matricePoint;
        MainStage.update();
    }

    public double getRapport() {
        return rapport;
    }

    public void setRapport(double rapport) {
        this.rapport = rapport;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Point getTmpPoint() {
        return tmpPoint;
    }

    public void setTmpPoint(Point tmpPoint) {
        this.tmpPoint = tmpPoint;
    }

    public boolean isRapportHorizontal() {
        return rapportHorizontal;
    }

    public void setRapportHorizontal(boolean rapportHorizontal) {
        this.rapportHorizontal = rapportHorizontal;
    }

    public boolean isTraitDessine() {
        return traitDessine;
    }

    public void setTraitDessine(boolean traitDessine) {
        this.traitDessine = traitDessine;
    }

    public boolean isFaceDessine() {
        return faceDessine;
    }

    public void setFaceDessine(boolean faceDessine) {
        this.faceDessine = faceDessine;
    }

    public boolean isLumiereActive() {
        return lumiereActive;
    }

    public void setLumiereActive(boolean lumiereActive) {
        this.lumiereActive = lumiereActive;
    }

    public boolean isTurning() {
        return isTurning;
    }

    public void setTurning(boolean turning) {
        isTurning = turning;
    }

    public Timeline getRotationAuto() {
        return rotationAuto;
    }

    public void setRotationAuto(Timeline rotationAuto) {
        this.rotationAuto = rotationAuto;
    }

}
