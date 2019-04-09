package model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@XmlRootElement
public class Kunde {

    public static AtomicInteger nextId = new AtomicInteger(1);

    @Id
    public int kundenID;

    @Basic
    public String vorname;


    public String nachname;

    @Embedded
    public Anschrift anschrift;


    public Boolean geschaeftskunde;



    public Boolean kundenkarte;


    public String email;




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
