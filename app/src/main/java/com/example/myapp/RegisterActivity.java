package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, email, password;
    private Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setToolbar();
        init();

    }

    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init(){
        username = findViewById(R.id.edt_register_username);
        email = findViewById(R.id.edt_register_email);
        password = findViewById(R.id.edt_register_password);
        btn_register = findViewById(R.id.btn_register_register);

        auth = FirebaseAuth.getInstance();
        btn_register.setOnClickListener(btn_register_Click);
    }

    private View.OnClickListener btn_register_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String edt_username = username.getText().toString();
            String edt_email = email.getText().toString();
            String edt_password = password.getText().toString();

            if(TextUtils.isEmpty(edt_username) || TextUtils.isEmpty(edt_email) || TextUtils.isEmpty(edt_password)){
                Toast.makeText(RegisterActivity.this, "All fileds are required", Toast.LENGTH_SHORT).show();
            }else if (edt_password.length() < 6) {
                Toast.makeText(RegisterActivity.this, "Your password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            }else {
                register(edt_username, edt_email, edt_password);
            }
        }
    };

    private void register(final String username, String email, String password){

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", username);
                    hashMap.put("imageURL", "default");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                }else {
                    Toast.makeText(RegisterActivity.this, "You can't register with this email and password. Try another!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
