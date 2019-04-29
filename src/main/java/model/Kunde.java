package model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Kunde {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int kundenID;

    @Basic
    public String vorname;


    public String nachname;

    @Embedded
    public Anschrift anschrift;


    public Boolean geschaeftskunde = false;



    public Boolean kundenkarte = true;


    public String email;


    public void setEmail(String email) {
        //email muss einem bestimmten Muster entsprechen, sonst wird null zurueck gegeben
        if (email.matches("[^@]+@[^\\.]+\\..+")){
            this.email = email;
        } else  {
            this.email = null;
        }
    }
}
