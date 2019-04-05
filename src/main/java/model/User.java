package model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.concurrent.atomic.AtomicInteger;

@XmlRootElement // required for XML binding
public class User {

    public static AtomicInteger nextId = new AtomicInteger(1);

    public int id;
    public String name;
    public String firstName;

    @JsonIgnore @XmlTransient
    public String passwordHash;

    @JsonIgnore @XmlTransient
    public byte[] image;

    public User() { }

    public User(String name, String firstName, String imagePath) {
        this.id = nextId.getAndIncrement();
        this.name = name;
        this.firstName = firstName;
        try {
            this.image = getClass().getResourceAsStream(imagePath).readAllBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
