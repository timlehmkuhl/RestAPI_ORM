package model;

import javax.persistence.*;
import java.util.Currency;

@Entity
public class Position {

    @Id
    public int id;

    public int artikelID;

    public int anzahl;

    public double preis;

    @ManyToOne
   // @JoinColumn(name="TEST_ID")
    public Kauf kauf;
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
