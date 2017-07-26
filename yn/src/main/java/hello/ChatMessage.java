package hello;

public class ChatMessage {
    private String name;
    private String content;
    private String idToken;
    private boolean currentUsersGreeting;
    //Todo: idToken and name exist in user object greeting should reference user instead!

    public ChatMessage() {
    }

    public ChatMessage(String name, String content, String idToken, boolean currentUsersGreeting) {
        this.name = name;
        this.content = content;
        this.idToken = idToken;
        this.currentUsersGreeting = currentUsersGreeting;
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

    public boolean isCurrentUsersGreeting() {
        return currentUsersGreeting;
    }

    public void setCurrentUsersGreeting(boolean currentUsersGreeting) {
        this.currentUsersGreeting = currentUsersGreeting;
    }
}