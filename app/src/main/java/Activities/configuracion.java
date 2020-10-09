package Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.social_gaming.R;

public class configuracion extends AppCompatActivity {
    public Switch switch_dark_mode;
    Button btn_guardar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cargarPreferencias_design();
        setContentView(R.layout.activity_configuracion);
        switch_dark_mode = findViewById(R.id.switch_dark_mode);
        btn_guardar = findViewById(R.id.btn_guardar_conf);
        cargarPreferencias();

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar_preferencias();
            }
        });





    }

    public void guardar_preferencias(){
        SharedPreferences preferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);

        int design_value = 0;
        if(switch_dark_mode.isChecked()){
            design_value = 1;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("design",design_value);
        editor.commit();
        Toast.makeText(this, "Preferencias guardadas", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);

    }

    public void cargarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("preferencias",Context.MODE_PRIVATE);
        int design_temp = preferences.getInt("design",0);
        Boolean set_design ;
        if (design_temp == 1){
            set_design = Boolean.TRUE;
        }else{
            set_design = Boolean.FALSE;
        }
        switch_dark_mode.setChecked(set_design);
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

    private void restartApp() {
        Intent i = new Intent(getApplicationContext(), configuracion.class);
        startActivity(i);
        finish();
    }
}