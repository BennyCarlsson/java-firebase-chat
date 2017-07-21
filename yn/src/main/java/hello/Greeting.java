package hello;

public class Greeting {
    private String name;
    private String content;
    private String idToken;
    //Todo: idToken and name exist in user object greeting should reference user instead!

    public Greeting() {
    }

    public Greeting(String name,String content,String idToken) {
        this.name = name;
        this.content = content;
        this.idToken = idToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
