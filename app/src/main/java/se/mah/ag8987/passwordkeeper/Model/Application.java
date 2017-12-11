package se.mah.ag8987.passwordkeeper.Model;

/**
 * Created by LinusHakansson on 2017-11-19.
 */

public class Application {
    private String name;
    private String password;
    private String key;

    public Application(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Application(String name, String password, String key) {
        this.name = name;
        this.password = password;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
