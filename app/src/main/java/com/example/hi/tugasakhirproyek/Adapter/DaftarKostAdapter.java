package com.example.hi.tugasakhirproyek.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hi.tugasakhirproyek.Model.kost;
import com.example.hi.tugasakhirproyek.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Hi on 20/01/2018.
 */

public class DaftarKostAdapter  extends RecyclerView.Adapter<DaftarKostAdapter.ViewHolder> {

    Context context;
    List<kost> kostList;
    DatabaseReference db;

    public DaftarKostAdapter(Context context, List<kost> kostList) {
        this.context = context;
        this.kostList = kostList;
    }
    @Override
    public DaftarKostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_daftar_kost,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DaftarKostAdapter.ViewHolder holder, int position) {
        db = FirebaseDatabase.getInstance().getReference().child("kost");
        FirebaseRecyclerAdapter<kost,DaftarKostAdapter.ViewHolder>
                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<kost,ViewHolder>(kost.class,R.layout.row_daftar_kost,ViewHolder.class,db)
        {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, kost model, int position) {
                final String post_key = getRef(position).getKey();
                kost dataKost = kostList.get(position);
                viewHolder.tvNamaKost.setText(dataKost.getNama_kost());
                viewHolder.tvJenisKost.setText(dataKost.getJenis_kost());
                viewHolder.tvDeskripsi.setText(dataKost.getDeskripsi());
                viewHolder.tvAlamat.setText(dataKost.getAlamat_kost());
                viewHolder.tvStokKamar.setText(String.valueOf(dataKost.getStok_kamar()));

//                viewHolder.textHarga.setText(String.valueOf(dataKost.getBiaya_sewa()));

                Glide.with(context).load(dataKost.getFoto()).into(viewHolder.imageView);


//                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
////                        Toast.makeText(context,post_key,Toast.LENGTH_SHORT).show();
//
////                        Intent deskripsiIntent = new Intent(context, DeskripsiActivity.class);
//
//                    }
//                });
            }
        };

        kost dataKost = kostList.get(position);
        holder.tvNamaKost.setText(dataKost.getNama_kost());
        holder.tvJenisKost.setText(dataKost.getJenis_kost());
        holder.tvDeskripsi.setText(dataKost.getDeskripsi());
        holder.tvAlamat.setText(dataKost.getAlamat_kost());
        holder.tvStokKamar.setText(String.valueOf(dataKost.getStok_kamar()));

//        holder.textHarga.setText(String.valueOf(dataKost.getHargaPerBulan()));

        Glide.with(context).load(dataKost.getFoto()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return kostList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public View mView;
        public ImageView imageView;
        public TextView tvNamaKost, tvJenisKost, tvDeskripsi, tvAlamat,tvStokKamar;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            tvNamaKost = itemView.findViewById(R.id.tvNamaKost);
            tvJenisKost = itemView.findViewById(R.id.tvJenisKost);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            tvAlamat = itemView.findViewById(R.id.tvAlamat);
            tvStokKamar = itemView.findViewById(R.id.tvStokKamar);

            mView = itemView;
        }
    }
}
