package com.example.hi.tugasakhirproyek.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hi.tugasakhirproyek.Model.pemesanan;
import com.example.hi.tugasakhirproyek.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Hi on 29/01/2018.
 */

public class PemesananAdapter extends RecyclerView.Adapter<PemesananAdapter.ViewHolder>{

    Context context;
    List<pemesanan> pemesananList;
    DatabaseReference db;

    public PemesananAdapter(Context context,List<pemesanan> pemesananList){
        this.context=context;
        this.pemesananList=pemesananList;
    }

    @Override
    public PemesananAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pemesanan,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PemesananAdapter.ViewHolder holder,int position){
        db= FirebaseDatabase.getInstance().getReference().child("pemesanan");
        FirebaseRecyclerAdapter<pemesanan, ViewHolder>
                firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<pemesanan,
                ViewHolder>(pemesanan.class,R.layout.row_pemesanan,ViewHolder.class,db)
        {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder,pemesanan model,int position){
                final String post_key=getRef(position).getKey();
                final pemesanan dataPemesanan=pemesananList.get(position);
                viewHolder.tvNamaKost.setText(dataPemesanan.getNama_kost());
                viewHolder.tvEmail.setText(dataPemesanan.getEmail());
                viewHolder.tvStatus.setText(dataPemesanan.getStatus());
                viewHolder.btKonfirmasi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reservasi(dataPemesanan.getId_pemesan(), dataPemesanan.getEmail(),
                                dataPemesanan.getId_kost(), dataPemesanan.getNama_kost(), "DITERIMA");
                    }
                });

            }
        };

        pemesanan dataPemesanan=pemesananList.get(position);
        holder.tvNamaKost.setText(dataPemesanan.getNama_kost());
        holder.tvEmail.setText(dataPemesanan.getEmail());
        holder.tvStatus.setText(dataPemesanan.getStatus());

    }

    @Override
    public int getItemCount(){
        return pemesananList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public View mView;
        public TextView tvNamaKost, tvEmail, tvStatus;
        public Button btKonfirmasi;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNamaKost = itemView.findViewById(R.id.tvNamaKost);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btKonfirmasi = itemView.findViewById(R.id.btKonfirmasi);

            mView = itemView;
        }
    }

    private void reservasi (String id_pemesanan, String email, String id_kost, String nama_kost, String status){
        pemesanan p = new pemesanan(id_pemesanan, email, id_kost, nama_kost, status);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("pemesanan");
        databaseReference.child(id_pemesanan).setValue(p);
    }
}
