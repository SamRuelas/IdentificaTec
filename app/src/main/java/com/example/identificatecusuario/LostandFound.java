package com.example.identificatecusuario;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.security.ConfirmationNotAvailableException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.app.Activity.RESULT_OK;

public class LostandFound extends Fragment {

    public LostandFound() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lostand_found, container, false);
    }

    Button found;
    Button lost;
    Converters converter = new Converters();
    final FirebaseDatabase db = FirebaseDatabase.getInstance();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        found = view.findViewById(R.id.found_credential_button);
        lost = view.findViewById(R.id.lost_credential_button);

        found.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(v.getContext(), MifareControl.class);

                 // Cambiar estado en credencial
                 intent.putExtra("mode", "writeExtraByteAB");
                 intent.putExtra("extraByteABSector", 3);
                 intent.putExtra("extraByteABSectorValue", "00");
                 startActivityForResult(intent, 0);

                 // Leer matricula
                 intent.putExtra("mode", "readManyDataBlocks");
                 intent.putExtra("readInitialBlock", 1);
                 intent.putExtra("blocksToRead", 1);
                 startActivityForResult(intent, 2);
             }
        });

        lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.getReference(FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0])
                        .child("isLost").setValue(false).addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Credencial registrada como perdida", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Received", "RC: " + requestCode + ", RESc" + resultCode);
        if(resultCode == RESULT_OK && requestCode == 2) {
            if (data.getBooleanExtra("readManyBlocksNoErrors", false)) {
                String received = data.getStringArrayListExtra("blocksRead").get(0).substring(14);
                String matricula = converter.stringFromHexAscii(received);
                DatabaseReference dbr = db.getReference(matricula.toUpperCase());
                try {
                    dbr.child("isLost").setValue(true);
                    dbr.child("foundBy").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0]).
                            addOnSuccessListener(
                                    new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(), "Credencial registrada como perdida", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
                }
                catch (Exception e){
                    Toast.makeText(getContext(), "Error al registrar como perdida", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
