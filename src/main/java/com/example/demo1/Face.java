package com.example.demo1;

import javafx.geometry.Point3D;

import java.util.ArrayList;

public class Face {

    ArrayList<Point3D> listePoints;

    public Face(){
        this.listePoints = new ArrayList<>();
    }

    public Face(ArrayList<Point3D> listePoints){
        this.listePoints = listePoints;
    }

}
