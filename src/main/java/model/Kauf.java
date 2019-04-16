package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@XmlRootElement // required for XML binding
public class Kauf {
    
    @Id
    public int kaufID;

    public int kundenID;

    public int baumarktID;

    public String zeit;

   @OneToMany(mappedBy="kauf", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Position> positions;

    public Kauf() {
        this.positions = new HashSet<>();

    }

    public int getKaufID() {
        return kaufID;
    }

    public void setKaufID(int kaufID) {
        this.kaufID = kaufID;
    }

    public int getKundenID() {
        return kundenID;
    }

    public void setKundenID(int kundenID) {
        this.kundenID = kundenID;
    }

    public int getBaumarktID() {
        return baumarktID;
    }

    public void setBaumarktID(int baumarktID) {
        this.baumarktID = baumarktID;
    }

    public String getZeit() {
        return zeit;
    }

    public void setZeit(String zeit) {
        this.zeit = zeit;
    }

    public Set<Position> getPositions() {
        return positions;
    }

    public void setPositions(Set<Position> positions) {
        this.positions = positions;
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
