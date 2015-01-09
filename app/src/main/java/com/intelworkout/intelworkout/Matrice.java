package com.intelworkout.intelworkout;

/**
 * Created by xavier on 09/01/15.
 */
public class Matrice {
    private int[][] tabMatrice;
    private String name ="";

    public Matrice(int[][] tab, String nom){
        this.tabMatrice = tab;
        this.name = nom;
    }

    public int getElementMatrice(int i, int j){
        return tabMatrice[i][j];
    }
    public String getNom(){
        return this.name;
    }
}
