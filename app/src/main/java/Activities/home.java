package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.social_gaming.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import Fragments.chatFragment;
import Fragments.filterFragment;
import Fragments.homeFragment;
import Fragments.profileFragment;

public class home extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cargarPreferencias_design();
        setContentView(R.layout.activity_home);
        bottomNavigation= findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(new homeFragment());

    }

    public void cargarPreferencias_design(){
        SharedPreferences preferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        int background = preferences.getInt("design",0);
        if (background ==1){
            setTheme(R.style.Theme_AppCompat_NoActionBar);
        }else{
            setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        }
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_home:
                        openFragment(new homeFragment());
                        return true;
                    case R.id.item_filters:
                        openFragment(new filterFragment());
                        return true;
                    /*case R.id.item_chat:
                        openFragment(new chatFragment());
                        return true;*/
                    case R.id.item_profile:
                        openFragment(new profileFragment());
                        return true;

                }
                return false;
            }
    };

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}