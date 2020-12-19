package com.example.tobo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class signup extends AppCompatActivity {

    EditText emailId,password,username;
    Button btnSignup;
    TextView tvlogin;
    FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth=FirebaseAuth.getInstance();
        username=findViewById(R.id.edname);
        emailId=findViewById(R.id.etemail);
        password = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);
        tvlogin=findViewById(R.id.etlogin);


        btnSignup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                String user = username.getText().toString();
                String email = emailId.getText().toString();
                String pwd = password .getText().toString();

                if(user.isEmpty()){
                    username.setError("Please enter your name");
                    username.requestFocus();
                }
                else if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Password not entered");
                }
                else if(user.isEmpty() && email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(signup.this,"Fields are empty!",Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && pwd.isEmpty())){

                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(!task.isSuccessful()){
                                Toast.makeText(signup.this,"Sign up unsuccessful!",Toast.LENGTH_SHORT).show();
                            }

                            else{
                                String user_name = username.getText().toString();
                                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                String user_id = current_user.getUid();
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                                HashMap<String, String> userMap = new HashMap<String, String>();
                                userMap.put("name", user_name);
                                userMap.put("status", getString(R.string.default_status));
                                userMap.put("image", "default");
                                userMap.put("thumb_image", "default");

                                mDatabase.setValue(userMap);

                                startActivity(new Intent(signup.this, HomeActivity.class));

                            }
                        }
                    });

                }
                else{
                    Toast.makeText(signup.this,"Error Occurred !",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(signup.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

}
