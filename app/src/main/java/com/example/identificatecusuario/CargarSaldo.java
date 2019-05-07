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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CargarSaldo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CargarSaldo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CargarSaldo extends Fragment {
    TextView textBalance;
    Button myButton;
    EditText text;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CargarSaldo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CargarSaldo.
     */
    // TODO: Rename and change types and number of parameters
    public static CargarSaldo newInstance(String param1, String param2) {
        CargarSaldo fragment = new CargarSaldo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    PayPalConfiguration config;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        String mat = user.getCurrentUser().getEmail().toUpperCase().split("@")[0];
        textBalance = view.findViewById(R.id.textBalance);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference(mat);
        db.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        textBalance.setText(dataSnapshot.child("balance").getValue().toString());
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
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cargar_saldo, container, false);

        return inflater.inflate(R.layout.fragment_cargar_saldo, container, false);
    }
    public View.OnClickListener onClicks = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String temp =text.getText().toString() ;
            if(temp == null || temp.isEmpty()){

            }else{
                double amount = Double.parseDouble(temp);

                PayPalPayment payment = new PayPalPayment(BigDecimal.valueOf(amount), "MXN", "IdentificaTec", PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(getContext(), PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                startActivityForResult(intent, 123);
            }

        }
    };



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123){
            if(resultCode== Activity.RESULT_OK){
                //CARGAR DINERO A CREDENCIAL
            }

        }
    }

    @Override
    public void onDestroy() {
        getContext().stopService(new Intent(getContext(), PayPalService.class));
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
