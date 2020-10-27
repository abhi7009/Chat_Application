package com.example.chat_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

import java.util.HashMap;

public class Register_Activity extends AppCompatActivity {

    MaterialEditText username,email,password;
    Button register;
    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");
        progressDialog.setCanceledOnTouchOutside(false);


        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username_txt = username.getText().toString();
                String email_txt = email.getText().toString();
                String password_txt = password.getText().toString();

                if(TextUtils.isEmpty(username_txt) || TextUtils.isEmpty(email_txt) || TextUtils.isEmpty(password_txt)){
                    Toast.makeText(Register_Activity.this, "All fields are required ", Toast.LENGTH_SHORT).show();
                }else if(password_txt.length() < 6){
                    Toast.makeText(Register_Activity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                }else {
                    register(username_txt,email_txt,password_txt);
                }

            }
        });

    }

    private void register(final String username, String email, String password){

        progressDialog.show();

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = auth.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users")
                                    .child(userid);
                            HashMap<String ,String > hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",username);
                            hashMap.put("image_url","default");
                            hashMap.put("status","offline");
                            hashMap.put("search",username.toLowerCase());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(Register_Activity.this,Welcome_Activity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(Register_Activity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);

    }
}