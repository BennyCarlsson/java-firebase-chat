package hello;

/**
 * Created by ABrownApple on 01/07/2017.
 */
public class Post {
    private String name;
    private String content;

    public Post(){

    }
    public Post(String name, String content){
        this.name = name;
        this.content = content;
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
}
