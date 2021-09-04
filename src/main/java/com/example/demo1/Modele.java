package com.example.demo1;

import javafx.geometry.Point3D;

import java.util.ArrayList;

public class Modele {

    ArrayList<Point3D> listePoints;
    ArrayList<Face> listeFace;

    public Modele(){
        this.listeFace = new ArrayList<>();
        this.listePoints = new ArrayList<>();
    }

    public Modele(ArrayList<Point3D> listePoints){
        this.listeFace = new ArrayList<>();
        this.listePoints = listePoints;
    }

    public Modele(ArrayList<Point3D> listePoints, ArrayList<Face> listeFace ){
        this.listeFace = listeFace;
        this.listePoints = listePoints;
    }

    public void addPoints(Point3D point){
        this.listePoints.add(point);
    }

    public void addPoints(ArrayList<Point3D> points){
        this.listePoints.addAll(points);
    }

    public void addFace(Face face){
        this.listeFace.add(face);
    }

    public void addFace(ArrayList<Face> faces){
        this.listeFace.addAll(faces);
    }

    public ArrayList<Point3D> getListePoints() {
        return listePoints;
    }

    public ArrayList<Face> getListeFace() {
        return listeFace;
    }


}
