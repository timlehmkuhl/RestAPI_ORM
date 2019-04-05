package model;

import java.util.Date;

public class AccountEntry {

    public String subject;
    public int value;
    public Date date;

    public AccountEntry() {
        this.date = new Date();
    }
}
