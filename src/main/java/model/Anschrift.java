package model;

import javax.persistence.*;


@Embeddable
public class Anschrift {

    public String strasse;


    public String plz;


    public String ort;


    public void setPlz(String plz) {
        //plz muss einem bestimmten Muster entsprechen, sonst wird null zurueck gegeben
        if (plz.matches("[0-9][0-9][0-9][0-9][0-9]")){
            this.plz = plz;
        } else  {
            this.plz = null;
        }
    }


}
