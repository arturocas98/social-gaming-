package Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.social_gaming.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import Activities.MainActivity;
import Activities.post;
import Providers.AuthProvider;


public class homeFragment extends Fragment {

    View view;
    FloatingActionButton floatingActionButton;
    Toolbar mToolbar;
    AuthProvider auth;

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

        return view;
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
        if(item.getItemId() == R.id.itemLogout){
            logout();
        }

        return true;
    }

    private void logout() {
        auth.logout();
        Intent i =  new Intent(getContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(i);
    }
}