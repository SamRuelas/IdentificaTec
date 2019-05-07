package com.example.identificatecusuario;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.function.Consumer;


public class MovimientosFragment extends Fragment {
    TextView textBalance;
    private ArrayList<String[]> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mFechas = new ArrayList<>();
    public MovimientosFragment() {
        // Required empty public constructor

    }

    private void initImageBitmaps(){
        //Cargamos información
       /* mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/identificatec-5a952.appspot.com/o/VerdeORojo%2Frojo.jpg?alt=media&token=ce149be0-a700-4eaf-92a8-4a4c9b4d7566");
        mNames.add("Rojo");
        mFechas.add("dormir");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/identificatec-5a952.appspot.com/o/VerdeORojo%2Fverde.jpg?alt=media&token=27e62e49-5dcd-4de5-9666-296d70c92561");
        mNames.add("Verde");
        mFechas.add("ahora");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/identificatec-5a952.appspot.com/o/VerdeORojo%2Fverde.jpg?alt=media&token=27e62e49-5dcd-4de5-9666-296d70c92561");
        mFechas.add("ahora");
*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movimientos, container, false);

        final FirebaseAuth user = FirebaseAuth.getInstance();
        String mat = user.getCurrentUser().getEmail().toUpperCase().split("@")[0];
        textBalance = view.findViewById(R.id.textBalance);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference(mat);
        db.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        textBalance.setText(dataSnapshot.child("balance").getValue().toString());
                        for (DataSnapshot d : dataSnapshot.child("charges").getChildren()) {
                         mNames.add(new String[] {d.getValue().toString()});
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
        initImageBitmaps();
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        //RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mNames, mImageUrls, mFechas);
        //recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // or
        //getActivity().findViewById(R.id.yourId).setOnClickListener(this);
    }

}