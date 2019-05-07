package com.example.identificatecusuario;

import android.content.Context;
import android.print.PrinterId;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mCantidades = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String > mFechas = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, ArrayList<String> mCantidades, ArrayList<String> mImages, ArrayList<String> mFechas) {
        this.mCantidades = mCantidades;
        this.mImages = mImages;
        this.mContext = mContext;
        this.mFechas = mFechas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);
        holder.cantidad.setText(mCantidades.get(position));
        holder.fechas.setText(mFechas.get(position));
    }

    @Override
    public int getItemCount() {

        return mCantidades.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image ;
        TextView cantidad;
        TextView fechas;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            cantidad = itemView.findViewById(R.id.cantidad);
            fechas = itemView.findViewById(R.id.fechas);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
