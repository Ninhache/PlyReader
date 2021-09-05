package com.example.viewer;

import javafx.css.Match;
import javafx.geometry.Point3D;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String elementHeader        = "end_header";


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

    public Modele getPointsFromPly(){

        Point.resetNAuto();
        Modele modele = new Modele(this.nbVertex);
        String[] tmpTab;

        while (sc.hasNextLine()) {
            try {

                String tmp = sc.nextLine();
                tmpTab = tmp.split("\\s+");

                if (modele.getListePoint().size() < this.nbVertex) { // POINT PART
                    modele.getListePoint().add(new Point(Double.parseDouble(tmpTab[0]),Double.parseDouble(tmpTab[1]),Double.parseDouble(tmpTab[2])));
                }else{

                    ArrayList<Point> tmpPoint = new ArrayList<>();
                    for(int i = 0 ; i <= Double.parseDouble(tmpTab[0]) ; i ++ ){
                        tmpPoint.add(modele.getListePoint().get(Integer.parseInt(tmpTab[i])));
                    }
                    Face f = new Face(tmpPoint);
                    modele.getListeFace().add(f);
                }

            }catch (Exception e){
                System.out.println(e);
            }
        }


        double rapport = 0;
        boolean rapportHorizontal = false;

        if(modele.getMaxX() - modele.getMinX() > modele.getMaxY() - modele.getMinY()) {
            rapport = modele.getMaxX() - modele.getMinX();
            rapportHorizontal = true;
        }else{
            rapport = modele.getMaxY() - modele.getMinY();
        }
        modele.setRapportHorizontal(rapportHorizontal);
        modele.setRapport(rapport);
        modele.setTmpPoint(new Point((modele.getMinX() + modele.getMaxX())/2, (modele.getMinY() + modele.getMaxY())/2, (modele.getMinZ()+ modele.getMaxZ()) / 2));
        modele.initMatrice();
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
