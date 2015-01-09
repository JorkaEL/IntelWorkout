package com.intelworkout.intelworkout;

/**
 * Created by xavier on 09/01/15.
 */
public class MatriceData {

    private long id;
    private String pseudo;
    private String temps;
    private String matrice;

    //Setteurs
    public void setId(long id){
        this.id = id;
    }
    public void setPseudo(String logging){
        this.pseudo = logging;
    }
    public void setTime(String time){
        this.temps = time;
    }
    public void setMatrice(String matrice){
        this.matrice = matrice;
    }

    //Getteurs
    public long getId(){
        return id;
    }
    public String getPseudo(){
        return pseudo;
    }
    public String getTemps(){
        return temps;
    }
    public String getMatrice(){
        return this.matrice;
    }

    public String toString(){
        String strData = matrice +": " + pseudo + " " + temps;
        return strData;

    }

}
