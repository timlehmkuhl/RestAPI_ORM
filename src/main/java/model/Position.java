package model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name="positions")
public class Position {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int positionID;

    public int artikelID;

    public int anzahl;


    @Column(name = "preis",precision=10, scale=0)
    public double preis;


    @ManyToOne
    @JoinColumn(name="fk")
    @XmlTransient
    @JsonIgnore
    public Kauf kauf;


    @XmlTransient
    @JsonIgnore
    public void setKauf(Kauf kauf) {
        this.kauf = kauf;
    }

    public void setPreis(double preis) {
        String str = String.valueOf(preis);
        if (str.substring(str.indexOf(".")+1).length() > 2) {
            BigDecimal bd = new BigDecimal(preis).setScale(2, RoundingMode.FLOOR);
            this.preis = bd.doubleValue();
        } else {
            this.preis = preis;
        }

    }

}
