package Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.social_gaming.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

import Activities.MainActivity;
import Activities.acerca_de;
import Activities.configuracion;
import Activities.post;
import Models.Post;
import Providers.AuthProvider;
import Providers.PostProvider;
import adapters.PostsAdapter;


public class homeFragment extends Fragment {

    View view;
    FloatingActionButton floatingActionButton;
    Toolbar mToolbar;
    AuthProvider auth;
    RecyclerView mRecyclerView;
    PostProvider mPostProvider;
    PostsAdapter mPostsAdapter;


    public homeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_home,container,false);
       floatingActionButton = view.findViewById(R.id.fab);
       mToolbar = view.findViewById(R.id.toolbar);
       //obtenemos el toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).setTitle("Publicaciones");
        //main menu
        setHasOptionsMenu(true);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPost();
            }
        });
        auth = new AuthProvider();
        mRecyclerView = view.findViewById(R.id.recyclerViewHome);
        // el linear nos va a poner las cardaview una debajo de otra
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mPostProvider = new PostProvider();

        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
        getAllPost();
    }
    private void getAllPost() {
        Query query = mPostProvider.getAll();
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
        mPostsAdapter = new PostsAdapter(options, getContext());
        //mPostsAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mPostsAdapter);
        mPostsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPostsAdapter.stopListening();
    }

    private void goToPost() {
        Intent intent = new Intent(getContext(), post.class);
        startActivity(intent);
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       /* if(item.getItemId() == R.id.itemLogout){
            logout();
        }*/
        switch (item.getItemId()){

            case R.id.itemConf :
                Intent i =  new Intent(getContext(), configuracion.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(i);
                return true;

            case R.id.itemAcerca:
                Intent intent = new Intent(getContext(), acerca_de.class);
                startActivityForResult(intent,0);
                return true;

            case R.id.itemLogout :
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        //return true;
    }

    private void logout() {
        auth.logout();
        Intent i =  new Intent(getContext(), MainActivity.class);
        //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(i);
    }
}