package hello;
import com.google.firebase.database.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.io.IOException;

@Controller
public class GreetingController {
    DatabaseReference ref;

    @GetMapping("/greeting")
    public String greetingForm(Model model) {
        model.addAttribute("greeting", new Greeting());
        return "greeting";
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute Greeting greeting) throws IOException {
        if(ref == null){
            FireBase fireBase = new FireBase();
            ref = fireBase.getRef();
        }
        DatabaseReference refMessages = ref.child("messages");
        refMessages.setValue(greeting.getContent());
        return "result";
    }

}
