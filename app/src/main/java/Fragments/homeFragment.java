package Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.social_gaming.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import Activities.post;


public class homeFragment extends Fragment {

    View view;
    FloatingActionButton floatingActionButton;

    public homeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_home,container,false);
       floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPost();
            }
        });

        return view;
    }

    private void goToPost() {
        Intent intent = new Intent(getContext(), post.class);
        startActivity(intent);
    }
}