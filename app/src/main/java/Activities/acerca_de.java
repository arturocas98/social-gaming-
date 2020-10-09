package Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.social_gaming.R;

import java.util.ArrayList;

public class acerca_de extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cargarPreferencias_design();
        setContentView(R.layout.activity_acerca_de);

        ListView lv = (ListView) findViewById(R.id.list_view);
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Arturo Castro");
        arrayList.add("Jorge Martinez");
        arrayList.add("Erick Bone");
        ListAdapter la = new ArrayAdapter<String>(this,R.layout.list_row,arrayList);
        lv.setAdapter(la);


        //LOGICA DEL BOTON PARA REGRESAR AL LOGIN

        Button btn_acerca_login = (Button)findViewById(R.id.btn_acerca_login);
        btn_acerca_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(acerca_de.this,home.class);
                startActivityForResult(i,0);
            }
        });
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

}