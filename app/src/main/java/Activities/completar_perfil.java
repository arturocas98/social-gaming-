package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.social_gaming.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Models.User;
import Providers.AuthProvider;
import Providers.UserProvider;
import dmax.dialog.SpotsDialog;

public class completar_perfil extends AppCompatActivity {

    TextInputEditText mTextInputUsername;
    TextInputEditText mTextInputphone;
    Button mButtonRegister;
    AuthProvider auth;
    UserProvider db;
    AlertDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completar_perfil);

        mTextInputUsername = findViewById(R.id.textInputUsername);
        mTextInputphone = findViewById(R.id.txt_phone_complete);
        mButtonRegister = findViewById(R.id.btnRegister);

        auth = new AuthProvider();
        db = new UserProvider();
        loading = new SpotsDialog.Builder().setContext(this).setMessage("Espere un momento").setCancelable(false).build();
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

    }

    private void register() {
        String username = mTextInputUsername.getText().toString();
        String phone = mTextInputphone.getText().toString();
        if (!username.isEmpty()  && !phone.isEmpty() ) {
            updateUser(username,phone);
        }
        else {
            Toast.makeText(this, "Para continuar inserta todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUser(final String username,final String phone) {
        String id = auth.getUid();
        User user = new User();
        user.setUsername(username);
        user.setPhone(phone);
        user.setTimestamp(new Date().getTime());
        user.setId(id);
        loading.show();
        db.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loading.dismiss();
                if (task.isSuccessful()) {
                    Intent intent = new Intent(completar_perfil.this, home.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(completar_perfil.this, "No se pudo almacenar el usuario en la base de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
