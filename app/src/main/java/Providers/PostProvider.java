package Providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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

    public Query getAll() {
        return collection.orderBy("title", Query.Direction.DESCENDING);
    }

    public Query getPostByUser(String id) {
        return collection.whereEqualTo("user_id", id);
    }
    public Task<Void> delete(String id) {
        return collection.document(id).delete();
    }

    public Query getPostByCategoryAndTimestamp(String category) {
        return collection.whereEqualTo("category", category).orderBy("timestamp", Query.Direction.DESCENDING);
    }
}
