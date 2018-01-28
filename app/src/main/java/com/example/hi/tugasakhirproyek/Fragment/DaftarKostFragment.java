package com.example.hi.tugasakhirproyek.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.hi.tugasakhirproyek.Adapter.DaftarKostAdapter;
import com.example.hi.tugasakhirproyek.DetailDaftarKostActivity;
import com.example.hi.tugasakhirproyek.Model.kost;
import com.example.hi.tugasakhirproyek.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hi on 18/01/2018.
 */

public class DaftarKostFragment  extends Fragment{

    RecyclerView rvDaftarKost;
    DaftarKostAdapter mAdapter;
    private List<kost> kostList = new ArrayList<>();
    ProgressDialog progress;

    public DaftarKostFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_daftar_kost, container, false);

        rvDaftarKost = (RecyclerView) rootView.findViewById(R.id.rvDaftarKost);
        rvDaftarKost.setHasFixedSize(true);
        rvDaftarKost.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDaftarKost.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        //Assign Activity this to progress dialog
        progress = new ProgressDialog(getContext());
        //setting up message in progress dialog
        progress.setMessage("Loading Daftar Kost");
        //Showing progress dialog.
        progress.show();
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("kost");


       db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    kost dk = postSnapshot.getValue(kost.class);
                    kostList.add(dk);

                }

                FirebaseRecyclerAdapter<kost,DaftarKostAdapter.ViewHolder>
                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<kost,DaftarKostAdapter.ViewHolder>
                        (kost.class,R.layout.row_daftar_kost,DaftarKostAdapter.ViewHolder.class,db) {
                    @Override
                    protected void populateViewHolder(DaftarKostAdapter.ViewHolder viewHolder, kost model, int position) {
                        final String post_key = getRef(position).getKey();
                        kost dataKost = kostList.get(position);
                        viewHolder.tvNamaKost.setText(dataKost.getNama_kost());
                        viewHolder.tvJenisKost.setText(dataKost.getJenis_kost());
                        viewHolder.tvDeskripsi.setText(dataKost.getDeskripsi());
                        viewHolder.tvAlamat.setText(dataKost.getAlamat_kost());
                        viewHolder.tvStokKamar.setText(String.valueOf(dataKost.getStok_kamar()));
//                        viewHolder.textHarga.setText("Rp. "+ String.valueOf(dataKost.getHargaPerBulan())+ ".000,-");

                        Glide.with(DaftarKostFragment.this).load(dataKost.getFoto()).into(viewHolder.imageView);



                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                Toast.makeText(getContext(),post_key,Toast.LENGTH_SHORT).show();

                                Intent deskripsiIntent = new Intent(getContext(), DetailDaftarKostActivity.class);
                                deskripsiIntent.putExtra("kos_id",post_key);
                                startActivity(deskripsiIntent);

                            }
                        });
                    }
                };

//                mAdapter = new OneFragmentAdapter(firebaseRecyclerAdapter);

                rvDaftarKost.setAdapter(firebaseRecyclerAdapter);

                //Hiding the progress dialog
                progress.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progress.dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
