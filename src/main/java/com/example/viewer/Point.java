package com.example.viewer;

import javafx.geometry.Point3D;

public class Point extends Point3D {

    private int id;
    private double x,y,z;
    private static int nAuto = 0;

    public Point(double x, double y, double z){
        super(x,y,z);
        this.id = nAuto;
        nAuto++;
    }

    public int getId() {
        return id;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public static void resetNAuto(){
        nAuto = 0;
    }

    @Override
    public String toString() {
        return "Point N°" + this.id +" {x=" + x + ", y=" + y + ", z=" + z + '}';
    }
}
