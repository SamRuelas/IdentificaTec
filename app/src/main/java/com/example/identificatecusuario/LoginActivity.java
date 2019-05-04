package com.example.identificatecusuario;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    //Se llama el método al iniciar la app

    EditText email;
    EditText contra;
    Button boton;
    String serialNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        contra = findViewById(R.id.password);
        boton = findViewById(R.id.login);
        System.out.println(FirebaseAuth.getInstance().getCurrentUser());
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            startActivity(new Intent(LoginActivity.this, NavDrawerActivity.class));
            finish();
        }

        boton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String emailText = email.getText().toString().toUpperCase();
                        String contraText = contra.getText().toString();

                        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailText, contraText)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            try {
                                                // Write a message to the database
                                                DatabaseReference database = FirebaseDatabase.getInstance()
                                                        .getReference(emailText.split("@")[0] + "/serialNumber/");
                                                database.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        serialNum = dataSnapshot.getValue().toString();
                                                        Toast.makeText(getApplicationContext(), serialNum, Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(LoginActivity.this, MifareControl.class);
                                                        intent.putExtra("mode", "readUID");
                                                        startActivityForResult(intent, 1);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError error) {
                                                    }

                                                });
                                            } catch (Exception e) {
                                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Autenticación faillida.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                }
        );


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Received", "RC: " + requestCode + ", RESc" + resultCode);
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data.getStringExtra("UID").equals(serialNum)) {
                Toast.makeText(getApplicationContext(), "Acceso permitido", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, NavDrawerActivity.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Credencial errónea", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
