package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.social_gaming.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import Models.User;
import Providers.AuthProvider;
import Providers.UserProvider;

public class MainActivity extends AppCompatActivity {
    TextView go_register;
    TextInputEditText txt_email;
    TextInputEditText txt_password;
    Button btn_login;
    AuthProvider auth;
    private GoogleSignInClient authGoogle;
    private final int REQUEST_CODE_GOOGLE=1;
    SignInButton btn_google;
    UserProvider db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        go_register = (TextView) findViewById(R.id.register);
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        btn_login = findViewById(R.id.btn_login);
        auth = new AuthProvider();
        db = new UserProvider();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        authGoogle = GoogleSignIn.getClient(this,gso);
        btn_google=findViewById(R.id.btn_google);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        go_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,register.class);
                startActivity(i);
            }
        });

        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

    }

    //Login con correo y contraseña normal
    private void login(){
        String email = txt_email.getText().toString();
        String password = txt_password.getText().toString();
        auth.login(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(MainActivity.this,home.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this,"Login correcto",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"El email o la contraseña son incorrectos",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("campos","email:"+email);
        Log.d("campos","password:"+password);
    }

    //Aqui se abre una ventana donde google le pregunta con que cuenta desea registrarse
    private void signInGoogle(){
        Intent signIn = authGoogle.getSignInIntent();
        startActivityForResult(signIn,REQUEST_CODE_GOOGLE);
    }

    //Metodo sobreescrito donde aqui se hace la verificacion de la cuenta de google q selecciona el usuario
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQUEST_CODE_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("SUCCESS", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("ERROR", "Google sign in failed", e);
                // [START_EXCLUDE]

                // [END_EXCLUDE]
            }
        }
    }
    //Metodo para que una vez verificada la cuenta de google se autentifique con firebase donde recibe por parametro una cta de google
    private void firebaseAuthWithGoogle(GoogleSignInAccount googleSignInAccount) {
        // [START_EXCLUDE silent]

        // [END_EXCLUDE]

        auth.googleLogin(googleSignInAccount)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id = auth.getUid();
                            checkUserExist(id);
                            // Sign in success, update UI with the signed-in user's information

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this,"No se pudo iniciar sesión con google",Toast.LENGTH_SHORT).show();
                            Log.w("ERROR", "signInWithCredential:failure", task.getException());


                        }


                    }
                });
    }
    //verificamos si el usuario registrado con google ya existe en la bd caso contrario lo crea
    private void checkUserExist(final String id) {
        db.getUser(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    Intent intent = new Intent(MainActivity.this,home.class);
                    startActivity(intent);
                }else{
                    String email = auth.getEmail();
                    User user = new User();
                    user.setEmail(email);
                    user.setId(id);
                    db.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(MainActivity.this,completar_perfil.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(MainActivity.this,"No se pudo almacenar la información del usuario",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}