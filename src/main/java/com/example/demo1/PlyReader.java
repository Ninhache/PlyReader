package com.example.demo1;

import javafx.geometry.Point3D;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PlyReader {

    private static Scanner sc;
    private int line;

    private String author;
    private String description;
    private int nbVertex; // Points
    private int nbFace;

    public PlyReader() {}

    public boolean loadPly(String pathFile) throws FileNotFoundException {

        this.line = 0;

        String elementVertex        = "element vertex ";
        String elementFace          = "element face ";
        String elementAuthor        = "comment created by ";
        String elementDescription   = "comment ";
        String elementHeader   = "end_header";


        sc = new Scanner(new File(pathFile));

        String tmp;
        Boolean endHeader = false;

        if(!sc.nextLine().contains("ply")) return false;

        while (sc.hasNextLine() && !endHeader) {
            tmp = sc.nextLine();

            if (tmp.contains(elementVertex)) {
                this.nbVertex = Integer.parseInt(tmp.substring(elementVertex.length()));
            } else if (tmp.contains(elementFace)) {
                this.nbFace = Integer.parseInt(tmp.substring(elementFace.length()));
            } else if (tmp.contains(elementAuthor)) {
                this.author = tmp.substring(elementAuthor.length());
            } else if (tmp.contains(elementDescription) && !tmp.contains(elementAuthor)) {
                this.description = tmp.substring(elementDescription.length());
            } else if (tmp.equals(elementHeader)){
                endHeader = true;
        }
            line++;
        }

        if(this.nbFace == 0 || this.nbVertex == 0){
            return false;
        }
        return true;
    }

    public Modele getPointsFromPly(String pathFile){
        Modele modele = new Modele();


        String tmp = "";
        String[] tmpTab;
        int compteurDePoints = 0;

        while (sc.hasNextLine()){
            if(compteurDePoints < this.nbVertex){ // POINT PART
                tmpTab = sc.nextLine().split("\\s+");
                modele.addPoints(new Point3D(Integer.parseInt(tmpTab[0]), Integer.parseInt(tmpTab[0]), Integer.parseInt(tmpTab[0])));
                compteurDePoints++;
            }else{ // FACE PART
                ArrayList<Point3D> tmpPoints = new ArrayList<>();
                tmpTab = sc.nextLine().split("\\s+");
                for(int i = 0 ; i < Integer.parseInt(tmpTab[0]) ; i ++ ){
                    tmpPoints.add(modele.getListePoints().get(Integer.parseInt(tmpTab[i])));
                }

                modele.addFace(new Face(tmpPoints));
            }

        }
        return modele;
    }

    public static Scanner getSc() {
        return sc;
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

    public int getNbVertex() {
        return nbVertex;
    }

    public void setNbVertex(int vertex) {
        this.nbVertex = vertex;
    }

    public int getNbFace() {
        return nbFace;
    }

    public void setNbFace(int nbFace) {
        this.nbFace = nbFace;
    }
}
