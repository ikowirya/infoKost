package com.example.hi.tugasakhirproyek.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.hi.tugasakhirproyek.Adapter.PemesananAdapter;
import com.example.hi.tugasakhirproyek.DetailDaftarKostActivity;
import com.example.hi.tugasakhirproyek.Model.pemesanan;
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
 * Created by Hi on 29/01/2018.
 */

public class PemesananFragment extends Fragment {
    RecyclerView rvDaftarPemesanan;
    PemesananAdapter mAdapter;
    private List<pemesanan> pemesananList = new ArrayList<>();
    ProgressDialog progress;
    private Button btKonfirmasi;

    public PemesananFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pemesanan, container, false);

        rvDaftarPemesanan = (RecyclerView) rootView.findViewById(R.id.rvDaftarPemesanan);
        btKonfirmasi = (Button) rootView.findViewById(R.id.btKonfirmasi);
        rvDaftarPemesanan.setHasFixedSize(true);
        rvDaftarPemesanan.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDaftarPemesanan.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        //Assign Activity this to progress dialog
        progress = new ProgressDialog(getContext());
        //setting up message in progress dialog
        progress.setMessage("Loading Daftar Pemesanan");
        //Showing progress dialog.
        progress.show();
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("pemesanan");


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    pemesanan dk = postSnapshot.getValue(pemesanan.class);
                    pemesananList.add(dk);

                }

                FirebaseRecyclerAdapter<pemesanan,PemesananAdapter.ViewHolder>
                        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<pemesanan,PemesananAdapter.ViewHolder>
                        (pemesanan.class,R.layout.row_pemesanan,PemesananAdapter.ViewHolder.class,db) {
                    @Override
                    protected void populateViewHolder(final PemesananAdapter.ViewHolder viewHolder, pemesanan model, int position) {
                        final String post_key = getRef(position).getKey();
                        final pemesanan dataPemesanan = pemesananList.get(position);
                        viewHolder.tvNamaKost.setText(dataPemesanan.getNama_kost());
                        viewHolder.tvEmail.setText(dataPemesanan.getEmail());
                        viewHolder.tvStatus.setText(dataPemesanan.getStatus());

                        if (dataPemesanan.getStatus().equals("DITERIMA")){
                            viewHolder.btKonfirmasi.setVisibility(View.GONE);
                        }

                        viewHolder.btKonfirmasi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                reservasi(dataPemesanan.getId_pemesan(), dataPemesanan.getEmail(),
                                        dataPemesanan.getId_kost(), dataPemesanan.getNama_kost(), "DITERIMA");
                                Toast.makeText(getActivity(),"Konfirmasi Berhasil",Toast.LENGTH_SHORT).show();
                                viewHolder.btKonfirmasi.setVisibility(View.GONE);
                            }
                        });



                    }
                };

                rvDaftarPemesanan.setAdapter(firebaseRecyclerAdapter);

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

    private void reservasi (String id_pemesanan, String email, String id_kost, String nama_kost, String status){


        pemesanan p = new pemesanan(id_pemesanan, email, id_kost, nama_kost, status);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("pemesanan");
        databaseReference.child(id_pemesanan).setValue(p);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
