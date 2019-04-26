package model;

import com.fasterxml.jackson.annotation.JsonFormat;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@Entity
@XmlRootElement // required for XML binding
public class Kauf {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int kaufID;

    public int kundenID;

    public int baumarktID;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    public Date zeit;


   @OneToMany(mappedBy="kauf", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Position> positions;

    public Kauf() {
        this.zeit = new Date();
        this.positions = new LinkedList<>();

    }

    public List<Position> getPositions() {
        return positions;
    }


}
