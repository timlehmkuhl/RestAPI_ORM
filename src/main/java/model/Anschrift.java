package model;

import javax.persistence.*;


@Embeddable
public class Anschrift {



    public String strasse;


    public String plz;

    public String ort;

    /*public Anschrift(String strasse, int plz, String ort) {
        this.strasse = strasse;
        this.plz = plz;
        this.ort = ort;
    }*/



}
