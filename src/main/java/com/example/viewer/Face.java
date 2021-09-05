package com.example.viewer;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Objects;

public class Face implements Comparable<Face> {

    private ArrayList<Point> listePoint;
    private ExtendedColor color;

    public Face(ArrayList<Point> listePoint, Color c){
        this.color = new ExtendedColor(c);
        this.listePoint = listePoint;
    }
    public Face(){
        this(new ArrayList<Point>(), Color.GRAY);
    }

    public Face(Color c){
        this(new ArrayList<>(), c);
    }

    public Face(ArrayList<Point> listePoint){
        this(listePoint, Color.GRAY);
    }

    public void addPoint(Point p){
        this.listePoint.add(p);
    }

    public ArrayList<Point> getListePoint() {
        return listePoint;
    }

    public ExtendedColor getColor() {
        return color;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Face other = (Face) obj;
        if (this.listePoint.isEmpty() || other.listePoint.isEmpty())
            return false;
        if (this.listePoint.size() != other.listePoint.size())
            return false;
        int cpt = 0;
        for (Point point : other.listePoint) {
            if(!point.equals(this.listePoint.get(cpt)))
                return false;
            cpt++;
        }
        return true;
    }

    public int compareTo(Face other) {
        double moyZThis = 0.0;
        int cptPoint = 0;
        double moyZOther = 0.0;

        for(Point p : listePoint) {
            moyZThis += p.getZ();
            cptPoint++;
        }

        moyZThis = moyZThis / cptPoint;
        cptPoint = 0;

        for(Point p : other.listePoint) {
            moyZOther += p.getZ();
            cptPoint++;
        }

        moyZOther = moyZOther/cptPoint;
        if(moyZThis < moyZOther)
            return -1;
        else if(moyZThis > moyZOther)
            return 1;
        else
            return 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(listePoint, color);
    }

    public void setListePoint(ArrayList<Point> listePoint) {
        this.listePoint = listePoint;
    }

    public void setColor(ExtendedColor color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Face{" + "listePoint=" + listePoint + '}';
    }
}
