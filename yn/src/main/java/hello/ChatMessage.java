package hello;

public class ChatMessage {
    private String content;
    private User user;
    private boolean currentUsersMessage;
    //Todo: idToken and name exist in user object greeting should reference user instead!

    public ChatMessage() {
    }

    public ChatMessage(String name, String content, String idToken, boolean currentUsersGreeting) {
        this.user.setName(name);
        this.user.setIdToken(idToken);
        this.content = content;
        this.currentUsersMessage = currentUsersGreeting;
    }

    public String getName() {
        return user.getName();
    }

    public void setName(String name) {
        this.user.setName(name);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIdToken() {
        return this.user.getIdToken();
    }

    public void setIdToken(String idToken) {
        this.user.setIdToken(idToken);
    }

    public boolean isCurrentUsersMessage() {
        return currentUsersMessage;
    }

    public void setCurrentUsersMessage(boolean currentUsersMessage) {
        this.currentUsersMessage = currentUsersMessage;
    }
}
