package com.example.social_gaming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class register extends AppCompatActivity {
    CircleImageView btn_back ;
    TextInputEditText txt_user;
    TextInputEditText txt_email_reg;
    TextInputEditText txt_password_reg;
    TextInputEditText txt_conf_passwor_reg;
    Button btn_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_back = findViewById(R.id.btn_back);
        txt_user = findViewById(R.id.txt_user_reg);
        txt_email_reg = findViewById(R.id.txt_email_reg);
        txt_password_reg = findViewById(R.id.txt_password_reg);
        btn_register = findViewById(R.id.btn_register);

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
        String con_password = txt_conf_passwor_reg.getText().toString();
        if (!user.isEmpty() && !email.isEmpty() && !password.isEmpty() && !con_password.isEmpty()){
            if (isEmailValid(email)){
                Toast.makeText(this,"Se ha registrado correctamente",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"El correo es invalido",Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(this,"Se ha registrado correctamente",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Llene todos los campos para registrarse",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}