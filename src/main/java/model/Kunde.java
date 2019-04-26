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


    public Boolean geschaeftskunde;



    public Boolean kundenkarte;


    public String email;


}
