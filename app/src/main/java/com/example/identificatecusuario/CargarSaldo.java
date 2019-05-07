package com.example.identificatecusuario;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CargarSaldo extends Fragment {
    TextView textBalance;
    Button myButton;
    EditText text;
    String mat;
    String balance;

    public CargarSaldo() {
        // Required empty public constructor
    }


    PayPalConfiguration config;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)//Cambiar a production para sacarlo
                .clientId("AdVdIgjfckcC878KhDb4YK2zwdj9FhU6_RgRWqd-6ySPQcONk0EkQllatfm-5wd5qWjPImfkoRMtgHQj");

        Intent i = new Intent(getContext(), PayPalService.class);

        i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getContext().startService(i);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //View myView = inflater.inflate(R.layout.fragment_cargar_saldo, container, false);
        myButton = (Button) view.findViewById(R.id.buttonpaypal);
        myButton.setOnClickListener(onClicks);
        text = (EditText)view.findViewById(R.id.recarga);


        final FirebaseAuth user = FirebaseAuth.getInstance();
        mat = user.getCurrentUser().getEmail().toUpperCase().split("@")[0];
        textBalance = view.findViewById(R.id.textBalance);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference(mat);
        db.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        balance = dataSnapshot.child("balance").getValue().toString();
                        textBalance.setText(balance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cargar_saldo, container, false);
    }

    double amount;
    public View.OnClickListener onClicks = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String temp =text.getText().toString() ;
            if(temp == null || temp.isEmpty()){

            }else{
                amount = Double.parseDouble(temp);

                PayPalPayment payment = new PayPalPayment(BigDecimal.valueOf(amount), "MXN", "IdentificaTec", PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(getContext(), PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                startActivityForResult(intent, 123);
            }

        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123){
            if(resultCode== Activity.RESULT_OK){
                //CARGAR DINERO A CREDENCIAL

                //Leer saldo
                Intent intent = new Intent(getContext(), MifareControl.class);
                intent.putExtra("mode", "writeManyValueBlocks");
                ArrayList<Integer> writeValueBlocks = new ArrayList<>();
                writeValueBlocks.add((int)amount+Integer.parseInt(balance));
                intent.putExtra("writeValueBlocks", writeValueBlocks);
                intent.putExtra("initialBlock", 2);
                startActivityForResult(intent, 1);
            }
        }
        if (requestCode==1 && resultCode==Activity.RESULT_OK) {
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(mat);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int balance = Integer.parseInt(dataSnapshot.child("balance").getValue().toString());
                    ref.child("balance").setValue(balance+(int)amount);

                    ref.child("deposits").child(new SimpleDateFormat("dd:MM:yyyy HH:mm:ss").format(new Date()))
                            .setValue((int)amount);
                    Toast.makeText(getContext(), "Saldo actualizado", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onDestroy() {
        getContext().stopService(new Intent(getContext(), PayPalService.class));
        super.onDestroy();
    }
}
