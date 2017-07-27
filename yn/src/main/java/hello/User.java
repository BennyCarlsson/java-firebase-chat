package hello;

/**
 * Created by ABrownApple on 14/07/2017.
 */
public class User {
    private String name;
    private String idToken;

    public User(){

    }
    public User(String name, String tokenID) {
        this.name = name;
        this.idToken = tokenID;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
