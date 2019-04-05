package model;

import java.util.Calendar;
import java.util.Date;

public class JWTClaim {

    // registered JWT claim names, see https://tools.ietf.org/html/rfc7519#section-4.1
    public String sub; // subject
    public String name;
    public Date iat; // issued at
    public Date exp; // expiration time

    public JWTClaim() { }

    public JWTClaim(String sub, String name) {
        this.sub = sub;
        this.name = name;
        Calendar cal = Calendar.getInstance();
        this.iat = cal.getTime();
        cal.add(Calendar.HOUR, 1); // tokens are valid for 1 hour
        this.exp = cal.getTime();
    }
}
