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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Models.User;
import Providers.AuthProvider;
import Providers.UserProvider;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class register extends AppCompatActivity {
    CircleImageView btn_back ;
    TextInputEditText txt_user;
    TextInputEditText txt_email_reg;
    TextInputEditText txt_password_reg;
    TextInputEditText txt_conf_password_reg;
    TextInputEditText txt_phone;
    Button btn_register;
    AuthProvider auth;
    UserProvider db;
    AlertDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_back = findViewById(R.id.btn_back);
        txt_user = findViewById(R.id.txt_user_reg);
        txt_email_reg = findViewById(R.id.txt_email_reg);
        txt_password_reg = findViewById(R.id.txt_password_reg);
        txt_phone = findViewById(R.id.txt_phone_reg);
        txt_conf_password_reg =findViewById(R.id.txt_conf_password_reg);
        btn_register = findViewById(R.id.btn_register);
        auth= new AuthProvider();
        db = new UserProvider();
        loading = new SpotsDialog.Builder().setContext(this).setMessage("Espere un momento").setCancelable(false).build();


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void register(){
        String user = txt_user.getText().toString();
        String email = txt_email_reg.getText().toString();
        String password = txt_password_reg.getText().toString();
        String con_password = txt_conf_password_reg.getText().toString();
        String phone = txt_phone.getText().toString();
        if (!user.isEmpty() && !email.isEmpty() && !password.isEmpty() && !con_password.isEmpty() &&  !phone.isEmpty()){
            if (isEmailValid(email)){
                if (password.equals(con_password)){
                    if (password.length()>=6){
                        createUser(user,email,password,phone);
                    }else{
                        Toast.makeText(this,"La contraseña debe tener al menos 6 caracteres",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this,"El correo es invalido",Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this,"Llene todos los campos para registrarse",Toast.LENGTH_SHORT).show();
        }
    }
    //metodo para validar email
    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private void createUser(final String username,final String email, final String password,final String phone){
        //se crea el usuario en firebase authentication
        loading.show();
        auth.create(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //getUid obtendra el id del usuario q se registro en firebase
                    String id =  auth.getUid();

                    User user = new User();
                    user.setId(id);
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setPhone(phone);
                    user.setTimestamp(new Date().getTime());
                    //se crea el usuario en firestore
                    db.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            loading.dismiss();
                            if (task.isSuccessful()){

                                Intent intent = new Intent(register.this, home.class);
                                //Cuando el usuario de click hacia atras se van a limpiar el historial de pantallas
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(register.this,"El usuario no se pudo almacenar",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    loading.dismiss();
                    Toast.makeText(register.this,"No se pudo registrar el usuario",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}