package hello;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.*;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by ABrownApple on 27/06/2017.
 */
@Configuration
public class FireBase {
    DatabaseReference ref;

    @PostConstruct
    public void init() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("C:/Users/ABrownApple/Downloads/yesno-2dbf9-firebase-adminsdk-2n3pj-c66cce13c7.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                .setDatabaseUrl("https://yesno-2dbf9.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);

        ref = FirebaseDatabase
                .getInstance()
                .getReference("restricted_access/secret_document");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object document = dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("onCancelled");
                System.out.println(databaseError);
            }
        });
    }

    public DatabaseReference getRef(){
        return this.ref;
    }
}
