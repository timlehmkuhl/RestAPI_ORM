package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Currency;
@Entity
@Table(name="positions")
public class Position {

    @Id
    public int id;

    public int artikelID;

    public int anzahl;

    public int preis;


    @ManyToOne
    @JoinColumn(name="fk")
   @JsonIgnoreProperties
    public Kauf kauf;

    public Position() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArtikelID() {
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

    public void setPreis(int preis) {
        this.preis = preis;
    }

    public Kauf getKauf() {
        return kauf;
    }

    public void setKauf(Kauf kauf) {
        this.kauf = kauf;
    }

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
