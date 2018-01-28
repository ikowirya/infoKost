package com.example.hi.tugasakhirproyek.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hi.tugasakhirproyek.Fragment.InfoTerbaruFragment;
import com.example.hi.tugasakhirproyek.Model.info_terbaru;
import com.example.hi.tugasakhirproyek.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Hi on 20/01/2018.
 */

public class InfoTerbaruAdapter extends RecyclerView.Adapter<InfoTerbaruAdapter.ViewHolder> {
    Context context;
    List<info_terbaru> infoTerbaruList;
    DatabaseReference db;

    public InfoTerbaruAdapter(Context context, List<info_terbaru> infoTerbaruList) {
        this.context = context;
        this.infoTerbaruList = infoTerbaruList;
    }


    @Override
    public InfoTerbaruAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_info_terbaru,parent,false);

        InfoTerbaruAdapter.ViewHolder viewHolder = new InfoTerbaruAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(InfoTerbaruAdapter.ViewHolder holder, int position) {
        db = FirebaseDatabase.getInstance().getReference().child("info_tebaru");

        FirebaseRecyclerAdapter<info_terbaru,InfoTerbaruAdapter.ViewHolder>
                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<info_terbaru,InfoTerbaruAdapter.ViewHolder>(info_terbaru.class,R.layout.row_info_terbaru,InfoTerbaruAdapter.ViewHolder.class,db)
        {
            @Override
            protected void populateViewHolder(InfoTerbaruAdapter.ViewHolder viewHolder, info_terbaru model, int position) {
                final String post_key = getRef(position).getKey();
                info_terbaru infoTerbaru = infoTerbaruList.get(position);
                viewHolder.txtJudulGambar.setText(infoTerbaru.getJudul_gambar());

//                viewHolder.textHarga.setText(String.valueOf(dataKost.getBiaya_sewa()));

                Glide.with(context).load(infoTerbaru.getInfo_gambar()).into(viewHolder.imageViewInfo);


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context,post_key,Toast.LENGTH_SHORT).show();

//                        Intent deskripsiIntent = new Intent(context, DeskripsiActivity.class);

                    }
                });
            }
        };

        info_terbaru infoTerbaru = infoTerbaruList.get(position);
        holder.txtJudulGambar.setText(infoTerbaru.getJudul_gambar());

//        holder.textHarga.setText(String.valueOf(dataKost.getHargaPerBulan()));

        Glide.with(context).load(infoTerbaru.getInfo_gambar()).into(holder.imageViewInfo);

    }


    @Override
    public int getItemCount() {
        return infoTerbaruList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public View mView;
        public ImageView imageViewInfo;
        public TextView txtJudulGambar;
        public ViewHolder(View itemView) {
            super(itemView);
            imageViewInfo = itemView.findViewById(R.id.imageViewInfo);
            txtJudulGambar = itemView.findViewById(R.id.txtJudulGambar);

            mView = itemView;
        }
    }
}
