package Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.social_gaming.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import Activities.edit_profile;
import Models.Post;
import Providers.AuthProvider;
import Providers.PostProvider;
import Providers.UserProvider;
import adapters.MyPostsAdapter;
import adapters.PostsAdapter;
import de.hdodenhof.circleimageview.CircleImageView;


public class profileFragment extends Fragment {

    LinearLayout mLinearLayoutEditProfile;
    View mView;

    TextView mTextViewUsername;
    TextView mTextViewPhone;
    TextView mTextViewEmail;
    TextView mTextViewPostNumber;
    ImageView mImageViewCover;
    CircleImageView mCircleImageProfile;
    RecyclerView mRecyclerView;

    UserProvider mUsersProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;
    MyPostsAdapter myPostsAdapter;

    public profileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        mLinearLayoutEditProfile = mView.findViewById(R.id.linearLayoutEditProfile);
        mLinearLayoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditProfile();
            }
        });
        mTextViewEmail = mView.findViewById(R.id.textViewEmail);
        mTextViewUsername = mView.findViewById(R.id.textViewUsername);
        mTextViewPhone = mView.findViewById(R.id.textViewphone);
        mTextViewPostNumber = mView.findViewById(R.id.textViewPostNumber);
        mCircleImageProfile = mView.findViewById(R.id.circleImageProfile);
        mImageViewCover = mView.findViewById(R.id.imageViewCover);
        mUsersProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();
        mRecyclerView = mView.findViewById(R.id.recyclerViewMyPost);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        getUser();
        getPostNumber();
        return mView;

    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mPostProvider.getPostByUser(mAuthProvider.getUid());
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
        myPostsAdapter = new MyPostsAdapter(options, getContext());
        mRecyclerView.setAdapter(myPostsAdapter);
        myPostsAdapter.startListening();
    }

    /*private void getAllPost() {
        Query query = mPostProvider.getPostByUser(mAuthProvider.getUid());
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
        myPostsAdapter = new MyPostsAdapter(options, getContext());
        //mPostsAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(myPostsAdapter);
        myPostsAdapter.startListening();
    }*/

    @Override
    public void onStop() {
        super.onStop();
        myPostsAdapter.stopListening();
    }

    private void getUser() {
        mUsersProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("email")) {
                        String email = documentSnapshot.getString("email");
                        mTextViewEmail.setText(email);
                    }
                    if (documentSnapshot.contains("phone")) {
                        String phone = documentSnapshot.getString("phone");
                        mTextViewPhone.setText(phone);
                    }
                    if (documentSnapshot.contains("username")) {
                        String username = documentSnapshot.getString("username");
                        mTextViewUsername.setText(username);
                    }
                    if (documentSnapshot.contains("imageProfile")) {
                        String imageProfile = documentSnapshot.getString("imageProfile");
                        if (imageProfile != null) {
                            if (!imageProfile.isEmpty()) {
                                Picasso.with(getContext()).load(imageProfile).into(mCircleImageProfile);
                            }
                        }
                    }
                    if (documentSnapshot.contains("imageCover")) {
                        String imageCover = documentSnapshot.getString("imageCover");
                        if (imageCover != null) {
                            if (!imageCover.isEmpty()) {
                                Picasso.with(getContext()).load(imageCover).into(mImageViewCover);
                            }
                        }
                    }
                }
            }
        });
    }

    private void getPostNumber() {
        mPostProvider.getPostByUser(mAuthProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberPost = queryDocumentSnapshots.size();
                mTextViewPostNumber.setText(String.valueOf(numberPost));
            }
        });
    }

    private void goToEditProfile() {
        Intent intent = new Intent(getContext(), edit_profile.class);
        startActivity(intent);
    }


}