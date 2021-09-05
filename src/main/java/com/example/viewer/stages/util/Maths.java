package com.example.viewer.stages.util;

import javafx.geometry.Point3D;

// Fonctions mathématiques supplémentaires non fournies dans la classe Math
public class Maths {

    //clamp(5, 2, 8) = 5
    //clamp(1, 2, 8) = 2
    //clamp(9, 2, 8) = 8
    public static double clamp(double x, double a, double b) {
        return Math.min(Math.max(x, a), b);
    }

    //clamp01(20) = 1
    //clamp01(-5) = 0
    //clamp01(1.5) = 1.5
    public static double clamp01(double x) {
        return clamp(x, 0, 1);
    }

    // TODO Remove ???
    public static double map(double x, double startA, double endA, double startB, double endB) {
        return (x - startA) / (endA - startA) * (endB - startB) + startB;
    }

    public static class Matrice {
        private int nb_Lignes;
        private int nb_Col;
        private double M[][];

        public Matrice(Point3D p) {
            this.nb_Col = 3;
            this.nb_Lignes = 1;

            this.M = new double[nb_Lignes][nb_Col];
            this.M[0][0] = p.getX();
            this.M[0][1] = p.getY();
            this.M[0][2] = p.getZ();
        }

        public Matrice(double M[][]) {
            this.nb_Lignes = M.length;
            this.nb_Col = M[0].length;
            this.M = M;
        }

        public Matrice addition(Matrice m) {
            double vals[][];
            int l1 = this.nb_Lignes;
            int c1 = this.nb_Col;

            int l2 = m.getNb_Lignes();
            int c2 = m.getNb_Col();

            if(l1 != l2 || c1 != c2) {
                throw new IllegalArgumentException("Addition impossible : Problème de dimensions.");
            } else {
                vals = new double[l1][c1];
            }

            for (int i = 0; i < l1; i++) {
                for(int j = 0; j<c1; j++) {
                    vals[i][j] = this.M[i][j] + m.getM()[i][j];
                }
            }

            Matrice res = new Matrice(vals);
            return res;
        }

        public Matrice addition(Point3D p) {
            Matrice m = new Matrice(p);
            return this.addition(m);
        }

        public Matrice multiplication(Matrice m) {
            return this.multiplication(this, m);
        }

        public Matrice multiplication(Point3D p) {
            Matrice m = new Matrice(p);
            return this.multiplication(m);
        }

        public Matrice multiplication(Matrice m1, Matrice m2) {
            double vals[][];
            int l1 = m1.getNb_Lignes();
            int c1 = m1.getNb_Col();

            int l2 = m2.getNb_Lignes();
            int c2 = m2.getNb_Col();
            if(l1 == c2) {
                vals = new double [l2][c1];
            } else {
                throw new IllegalArgumentException("Pb dimensions");
            }

            for(int i = 0; i<l2; i++) {
                for(int j = 0; j<c1; j++) {
                    vals[i][j] = 0;
                    for(int k = 0; k<c2; k++) {
                        vals[i][j] = vals[i][j] + m2.getM()[i][k] * m1.getM()[k][j];
                    }
                }
            }

            Matrice res = new Matrice(vals);

            return res;
        }

        public Matrice multiplication(Matrice m, double scalaire) {
            double resTab[][] = new double[m.getNb_Lignes()][m.getNb_Col()];
            for(int i = 0; i < resTab.length; i++) {
                for(int j = 0; j < resTab[0].length; j++) {
                    if(i == resTab.length-1) {
                        resTab[i][j] = m.getM()[i][j];
                    } else {
                        resTab[i][j] = m.getM()[i][j]*scalaire;
                    }

                }
            }
            return new Matrice(resTab);
        }

        public Matrice homothétie(double rapportK) {
            double vals[][] = new double[][] {
                    {rapportK, 0, 0, 0},
                    {0, rapportK, 0, 0},
                    {0, 0, rapportK, 0},
                    {0, 0, 0, 1}
            };

            Matrice m = new Matrice(vals);

            return this.multiplication(m);
        }

        public Matrice translation(double t1, double t2, double t3) {
            double vals[][] = new double[][] {
                    {1, 0, 0, t1},
                    {0, 1, 0, t2},
                    {0, 0, 1, t3},
                    {0, 0, 0, 1}
            };

            Matrice m1 = new Matrice(vals);

            return multiplication(this, m1);
        }

        public Matrice rotation(Rotation r, double degre) {
            double vals[][] = new double[this.nb_Lignes][this.nb_Col];
            if(r.equals(Rotation.X)) {
                vals = new double[][] {
                        {1, 0, 0, 0},
                        {0, Math.cos(Math.toRadians(degre)), -Math.sin(Math.toRadians(degre)), 0},
                        {0, Math.sin(Math.toRadians(degre)), Math.cos(Math.toRadians(degre)), 0},
                        {0, 0, 0, 1},
                };
            } else if(r.equals(Rotation.Y)) {
                vals = new double[][] {
                        {Math.cos(Math.toRadians(degre)), 0, Math.sin(Math.toRadians(degre)), 0},
                        {0, 1, 0, 0},
                        {-Math.sin(Math.toRadians(degre)), 0, Math.cos(Math.toRadians(degre)), 0},
                        {0, 0, 0, 1},
                };
            } else if(r.equals(Rotation.Z)) {
                vals = new double[][] {
                        {Math.cos(Math.toRadians(degre)), -Math.sin(Math.toRadians(degre)), 0, 0},
                        {Math.sin(Math.toRadians(degre)), Math.cos(Math.toRadians(degre)), 0, 0},
                        {0, 0, 1, 0},
                        {0, 0, 0, 1},
                };
            } else {
                throw new IllegalArgumentException("Le type de rotation n'est pas valable.");
            }


            Matrice m = new Matrice(vals);
            //System.out.println("cos(90) = " + Math.cos(Math.toRadians(90.0)) + " sin(90) = " + Math.sin(Math.toRadians(degre)));
            return this.multiplication(m);
        }

        public Matrice multiplication(double scalaire) {
            return this.multiplication(this, scalaire);
        }

        public int getNb_Lignes() {
            return nb_Lignes;
        }

        public void setNb_Lignes(int nb_Lignes) {
            this.nb_Lignes = nb_Lignes;
        }

        public int getNb_Col() {
            return nb_Col;
        }

        public void setNb_Col(int nb_Col) {
            this.nb_Col = nb_Col;
        }

        public double[][] getM() {
            return M;
        }

        public void setM(double[][] m) {
            M = m;
        }
    }

    public static class Vecteur {

        private double x;
        private double y;
        private double z;


        public Vecteur(double anX, double anY, double aZ) {
            this.x = anX;
            this.y = anY;
            this.z = aZ;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getZ() {
            return z;
        }

        public void setZ(double z) {
            this.z = z;
        }

        public Vecteur produitVectoriel(Vecteur v2) {
            double newX = this.y * v2.getZ() - this.z * v2.getY();
            double newY = this.z * v2.getX() - this.x * v2.getZ();
            double newZ = this.x * v2.getY() - this.y * v2.getX();
            return new Vecteur(newX, newY, newZ);
        }

        public double produitScalaire(Vecteur v2) {
            return this.x * v2.x + this.y * v2.y + this.z * v2.z;
        }

        private double Norme() {
            return (Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z));
        }

        public Vecteur Normalisation() {
            double norme = Norme();
            this.x = this.x / norme;
            this.y = this.y / norme;
            this.z = this.z / norme;
            return this;
        }
    }



    public static enum Rotation {
        X("axeX"), Y("axeY"), Z("axeZ");

        private String s;

        private Rotation(String s) {
            this.s = s;
        }

        public String getRotation() {
            return this.s;
        }
    }
}
