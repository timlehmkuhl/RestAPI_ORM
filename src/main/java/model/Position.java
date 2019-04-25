package model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import javax.persistence.*;

import javax.xml.bind.annotation.XmlTransient;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

@Entity
@Table(name="positions")
public class Position {

    @Id
    public int id;

    public int artikelID;

    public int anzahl;

//@JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT, pattern = )
@Column(name = "preis",precision=10, scale=0)
    public double preis;


    @ManyToOne
    @JoinColumn(name="fk")
    @XmlTransient
   @JsonIgnore
    public Kauf kauf;

    public Position() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

  /*  public int getArtikelID() {
        return artikelID;
    }

    public void setArtikelID(int artikelID) {
        this.artikelID = artikelID;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    public double getPreis() {
        return preis;
    }



    @XmlTransient
    @JsonIgnore
    public Kauf getKauf() {
        return kauf;
    }*/



    /*
    <Kauf Kauf-ID="KAUF1">
			<Kunden-ID>K1</Kunden-ID>
			<Baumarkt-ID>B1</Baumarkt-ID>
			<Zeit>2018-02-17T09:30:47Z</Zeit>
			<Positionen>
				<Position>
					<Artikel-ID>A1</Artikel-ID>
					<Anzahl>3</Anzahl>
					<Preis>0.99</Preis>
				</Position>
				<Position>
					<Artikel-ID>A3</Artikel-ID>
					<Anzahl>4</Anzahl>
					<Preis>59.99</Preis>
				</Position>
			</Positionen>
		</Kauf>
     */

}
