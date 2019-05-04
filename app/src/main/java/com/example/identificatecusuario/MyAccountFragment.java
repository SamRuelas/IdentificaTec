package com.example.identificatecusuario;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {

    TextView textName;
    TextView textEmail;
    TextView textProgram;
    TextView textBalance;

    public MyAccountFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        final FirebaseAuth user = FirebaseAuth.getInstance();
        String mat = user.getCurrentUser().getEmail().toUpperCase().split("@")[0];
        textName = view.findViewById(R.id.textName);
        textEmail = view.findViewById(R.id.textEmail);
        textProgram = view.findViewById(R.id.textPrograma);
        textBalance = view.findViewById(R.id.textBalance);

        textEmail.setText(user.getCurrentUser().getEmail());

        DatabaseReference db = FirebaseDatabase.getInstance().getReference(mat);
        db.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //System.out.println("nombre :  "+dataSnapshot.child("name").getValue());
                        textName.setText(dataSnapshot.child("name").getValue().toString());
                        textProgram.setText(dataSnapshot.child("academicProgram").getValue().toString());
                        textBalance.setText(dataSnapshot.child("balance").getValue().toString());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );

        return view;
    }

}
