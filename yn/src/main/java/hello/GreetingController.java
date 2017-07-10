package hello;
import com.google.firebase.database.*;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class GreetingController {
    DatabaseReference ref;
    List<Post> posts = new ArrayList<>();
    @Autowired
    private SimpMessagingTemplate broker;

    @Autowired
    public GreetingController(final SimpMessagingTemplate broker) {
        this.broker = broker;
    }

    @GetMapping("/")
    public String start(Model model){
        model.addAttribute("greeting", new Greeting());
        return "index";
    }

    @MessageMapping("/saveDB")
    public void saveDB(Greeting greeting) throws IOException {
        if(ref == null){
            FireBase fireBase = new FireBase();
            ref = fireBase.getRef();
        }
        DatabaseReference refMessages = ref.child("messages");
        DatabaseReference pushRef = refMessages.push();
        pushRef.setValue(greeting);
        /*List<Post> posts = new ArrayList<>();
        posts.add(new Post("post1","content1"));
        posts.add(new Post("post2","content2"));
        posts.add(new Post("post3","content3"));
        broker.convertAndSend("/topic/posts",posts);*/
    }

    @MessageMapping("/hello")
    public Greeting greeting(HelloMessage message) throws IOException {
        if(ref == null){
            FireBase fireBase = new FireBase();
            ref = fireBase.getRef();
        }
        DatabaseReference refMessages = ref.child("messages");
        refMessages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                posts.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    posts.add(post);
                    broker.convertAndSend("/topic/greetings",new Greeting(post.getContent()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        return new Greeting("hi asd");
    }

    /*
    @PostMapping("/")
    public String greetingSubmit(@ModelAttribute Greeting greeting) throws IOException {
        return "index";
    }
    */
}
