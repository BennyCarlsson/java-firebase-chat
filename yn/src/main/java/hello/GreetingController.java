package hello;
import com.google.firebase.database.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GreetingController {
    DatabaseReference ref;
    List<Post> posts = new ArrayList<>();
    @GetMapping("/")
    public String greetingForm(Model model) throws IOException {
        model.addAttribute("greeting", new Greeting());
        if(ref == null){
            FireBase fireBase = new FireBase();
            ref = fireBase.getRef();
        }
        DatabaseReference refMessages = ref.child("messages");
        posts.add(new Post("TestName","test"));
        posts.add(new Post("TestName","test"));
        posts.add(new Post("TestName","test"));
        model.addAttribute("posts",posts);
        refMessages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                posts.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    posts.add(post);
                }
                model.addAttribute("posts",posts);
                //Post post = dataSnapshot.getValue(Post.class);
                //System.out.println(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        return "index";
    }

    @PostMapping("/")
    public String greetingSubmit(@ModelAttribute Greeting greeting) throws IOException {
        if(ref == null){
            FireBase fireBase = new FireBase();
            ref = fireBase.getRef();
        }
        DatabaseReference refMessages = ref.child("messages");
        DatabaseReference pushRef = refMessages.push();
        pushRef.setValue(greeting);
        return "index";
    }
}
