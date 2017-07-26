package hello;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.tasks.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class MessageController {
    @Autowired
    private SimpMessagingTemplate broker;
    @Autowired
    private FireBase fireBase;
    DatabaseReference ref;

    @Autowired
    public MessageController(final SimpMessagingTemplate broker, FireBase fireBase) {
        this.broker = broker;
        this.fireBase = fireBase;
        ref = fireBase.getRef();
    }

    @GetMapping("/")
    public String start(Model model){
        model.addAttribute("chatmessage", new ChatMessage());
        return "index";
    }

    @MessageMapping("/saveDB")
    public void saveDB(ChatMessage chatMessage) throws IOException {
        //chatMessage now have the idToken use this to get username and uid
        Task<FirebaseToken> authTask = FirebaseAuth.getInstance().verifyIdToken(chatMessage.getIdToken());
        try {Tasks.await(authTask);}
        catch(ExecutionException | InterruptedException e ){ System.out.print(e);}
        FirebaseToken decodedToken = authTask.getResult();
        chatMessage.setName(decodedToken.getName());
        chatMessage.setIdToken(decodedToken.getUid());
        DatabaseReference refMessages = ref.child("messages");
        DatabaseReference pushRef = refMessages.push();
        pushRef.setValue(chatMessage);


    }
    @MessageMapping("firebaselistener")
    public void firebaseListener(User user) throws IOException {
        FirebaseAuth.getInstance().verifyIdToken(user.getIdToken())
                .addOnSuccessListener(new OnSuccessListener<FirebaseToken>() {
                    @Override
                    public void onSuccess(FirebaseToken firebaseToken) {
                        String uid = firebaseToken.getUid();
                        DatabaseReference refMessages = ref.child("messages");
                        refMessages.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                List<ChatMessage> chatMessages = new ArrayList<>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                                    if(chatMessage.getIdToken().equals(uid)){
                                        chatMessage.setCurrentUsersGreeting(true);
                                    }else{
                                        chatMessage.setCurrentUsersGreeting(false);
                                    }
                                    chatMessages.add(chatMessage);
                                }
                                broker.convertAndSend("/topic/greetingList", chatMessages);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                });
    }
}
