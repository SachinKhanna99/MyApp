package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StartActivity extends AppCompatActivity {

    private Button btn_register, btn_login;

    FirebaseAuth auth;
    private EditText email, password;

    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        checkCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        init();

        btn_register.setOnClickListener(btnRegisterClick);
        btn_login.setOnClickListener(btnLoginClick);
    }

    private void init(){
        email = findViewById(R.id.edt_login_email);
        password = findViewById(R.id.edt_login_password);
        btn_register = findViewById(R.id.btn_login_register);
        btn_login = findViewById(R.id.btn_login_login);

        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void checkCurrentUser(){
        if(firebaseUser != null){
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private View.OnClickListener btnLoginClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String edt_email = email.getText().toString();
            String edt_password = password.getText().toString();

            if(TextUtils.isEmpty(edt_email) || TextUtils.isEmpty(edt_password)){
                Toast.makeText(StartActivity.this, "All fileds are required", Toast.LENGTH_SHORT).show();
            }else {
                auth.signInWithEmailAndPassword(edt_email, edt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(StartActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(StartActivity.this, "Incorrect account, please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    };

    private View.OnClickListener btnRegisterClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    };
}
