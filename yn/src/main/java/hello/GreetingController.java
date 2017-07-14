package hello;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.tasks.OnFailureListener;
import com.google.firebase.tasks.OnSuccessListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GreetingController {
    @Autowired
    private SimpMessagingTemplate broker;
    @Autowired
    private FireBase fireBase;
    DatabaseReference ref;

    @Autowired
    public GreetingController(final SimpMessagingTemplate broker, FireBase fireBase) {
        this.broker = broker;
        this.fireBase = fireBase;
        ref = fireBase.getRef();
    }

    @GetMapping("/")
    public String start(Model model){
        model.addAttribute("greeting", new Greeting());
        return "index";
    }

    @MessageMapping("/saveDB")
    public void saveDB(Greeting greeting) throws IOException {
        DatabaseReference refMessages = ref.child("messages");
        DatabaseReference pushRef = refMessages.push();
        pushRef.setValue(greeting);
    }
    @MessageMapping("firebaselistener")
    public void firebaseListener(User user) throws IOException {
        FirebaseAuth.getInstance().verifyIdToken(user.getIdToken())
                .addOnSuccessListener(new OnSuccessListener<FirebaseToken>() {
                    @Override
                    public void onSuccess(FirebaseToken decodedToken) {
                        String uid = decodedToken.getUid();
                        DatabaseReference refMessages = ref.child("messages");
                        refMessages.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                List<Greeting> greetings = new ArrayList<>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Greeting greeting = snapshot.getValue(Greeting.class);
                                    greetings.add(greeting);
                                }
                                broker.convertAndSend("/topic/greetingList",greetings);
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
