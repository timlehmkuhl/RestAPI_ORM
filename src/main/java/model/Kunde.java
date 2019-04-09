package model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@XmlRootElement
public class Kunde {

    public static AtomicInteger nextId = new AtomicInteger(1);


    public int kundenID;

    public String vorname;

    public String nachname;

    public Anschrift anschrift;

    public Boolean geschaeftskunde;

    public Boolean kundenkarte;

    public String email;

    public static AtomicInteger getNextId() {
        return nextId;
    }

    public static void setNextId(AtomicInteger nextId) {
        Kunde.nextId = nextId;
    }

    public int getKundenID() {
        return kundenID;
    }

    public void setKundenID(int kundenID) {
        this.kundenID = kundenID;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public Anschrift getAnschrift() {
        return anschrift;
    }

    public void setAnschrift(Anschrift anschrift) {
        this.anschrift = anschrift;
    }

    public Boolean getGeschaeftskunde() {
        return geschaeftskunde;
    }

    public void setGeschaeftskunde(Boolean geschaeftskunde) {
        this.geschaeftskunde = geschaeftskunde;
    }

    public Boolean getKundenkarte() {
        return kundenkarte;
    }

    public void setKundenkarte(Boolean kundenkarte) {
        this.kundenkarte = kundenkarte;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


 /*

    <Kunde Kunden-ID="K1">
			<Vorname>Max</Vorname>
			<Nachname>Müller</Nachname>
			<Anschrift>
				<Strasse>Schwartauer Allee 4</Strasse>
				<PLZ>23565</PLZ>
				<Ort>Lübeck</Ort>
			</Anschrift>
			<Geschäftskunde>false</Geschäftskunde>
			<Kundenkarte>false</Kundenkarte>
			<E-Mail>max.müllergmx.de</E-Mail>

     */
}
