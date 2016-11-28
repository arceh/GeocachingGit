package hsnr.arcehfabencasob.www.geocaching.DBS;

/**
 * Created by Carsten on 28.11.2016.
 */

public class User {

    public String username;
    public String password;
    public int id;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(int id, String username, String password) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

}
