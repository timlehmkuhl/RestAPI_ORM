package model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@XmlRootElement // required for XML binding
public class Account {

    public static AtomicInteger nextId = new AtomicInteger(1);

    public int id;
    public String owner;
    public List<AccountEntry> entries;

    /*public int balance() {
        if (entries == null) return 0;
        return entries.stream().mapToInt(entry -> entry.value).sum();
    }*/
}
