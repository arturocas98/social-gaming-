package com.example.social_gaming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    TextView go_register;
    TextInputEditText txt_email;
    TextInputEditText txt_password;
    Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        go_register = (TextView) findViewById(R.id.register);
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        btn_login = findViewById(R.id.btn_login);

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

    }

    private void login(){
        String email = txt_email.getText().toString();
        String password = txt_password.getText().toString();
        Log.d("campos","email:"+email);
        Log.d("campos","password:"+password);
    }
}