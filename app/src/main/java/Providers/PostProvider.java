package Providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;

import Models.Post;

public class PostProvider {
    CollectionReference collection;
    public PostProvider(){
       collection = FirebaseFirestore.getInstance().collection("Posts");
    }

    public Task<Void> save(Post post){
        return collection.document().set(post);
    }
}
