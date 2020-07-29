package com.example.selfjournalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.selfjournalapp.util.JournalApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class createAccountActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText email;
    private Button createAccount;
    private ProgressBar progressBar;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //Firestore Connection
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    private CollectionReference collectionReference=db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        username=findViewById(R.id.username_account);
        password=findViewById(R.id.password_account);
        progressBar=findViewById(R.id.create_acct_progress);
        email=findViewById(R.id.email_account);
        createAccount=findViewById(R.id.create_account_button);

        firebaseAuth=FirebaseAuth.getInstance();

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if(currentUser!=null){
                    //user is already logged in

                }
                else{
                    //user not logged in
                }

            }
        };

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(email.getText().toString())
                        && !TextUtils.isEmpty(password.getText().toString())
                        && !TextUtils.isEmpty(username.getText().toString())) {
                    String emailId=email.getText().toString().trim();
                    String name=username.getText().toString().trim();
                    String pw=password.getText().toString().trim();

                    createUserEmailAccount(emailId,pw,name);
                }
                else{
                    Toast.makeText(createAccountActivity.this, "Empty fields not allowed", Toast.LENGTH_SHORT).show();

                }                    
            }
        });
    }

    private void createUserEmailAccount(String emailId, String pw, final String username) {

        if(!TextUtils.isEmpty(emailId)
        && !TextUtils.isEmpty(pw)
        && !TextUtils.isEmpty(username)){
            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(emailId,pw)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                currentUser=firebaseAuth.getCurrentUser();
                                Toast.makeText(createAccountActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(createAccountActivity.this,));
                                final String currentUserId=currentUser.getUid();

                                Map<String,String> userObj=new HashMap<>();
                                userObj.put("userId",currentUserId);
                                userObj.put("username",username);

                                collectionReference.add(userObj)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if(Objects.requireNonNull(task.getResult()).exists()){
                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                    String name=task.getResult()
                                                                            .getString("username");

                                                                    JournalApi journalApi=JournalApi.getInstance();
                                                                    journalApi.setUserId(currentUserId);
                                                                    journalApi.setUsername(name);

                                                                    Intent intent=new Intent(createAccountActivity.this,PostJournalActivity.class);
                                                                    intent.putExtra("username",name);
                                                                    intent.putExtra("userID",currentUserId);
                                                                    startActivity(intent);

                                                                }else {

                                                                }

                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });


                            }   else{
                                Log.d("CreateAccountActivity", "User not created ");
                                Toast.makeText(createAccountActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
        else{
            Toast.makeText(createAccountActivity.this, "Credentials missing", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
