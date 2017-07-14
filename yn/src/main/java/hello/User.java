package hello;

/**
 * Created by ABrownApple on 14/07/2017.
 */
public class User {
    private String idToken;

    public User(){

    }
    public User(String tokenID) {
        this.idToken = tokenID;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
