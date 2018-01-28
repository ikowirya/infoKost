package com.example.hi.tugasakhirproyek.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.hi.tugasakhirproyek.Adapter.InfoTerbaruAdapter;
import com.example.hi.tugasakhirproyek.DetailInfoTerbaruActivity;
import com.example.hi.tugasakhirproyek.Model.info_terbaru;
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

public class InfoTerbaruFragment extends Fragment {
    FloatingActionButton fab;
    Context context;
    InfoTerbaruAdapter mAdapter;
    RecyclerView rvDaftarInfoTerbaru;
    private List<info_terbaru> infoTerbaruList= new ArrayList<>();
    DatabaseReference db;
    ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info_terbaru, container, false);

        rvDaftarInfoTerbaru = (RecyclerView) rootView.findViewById(R.id.rvDaftarInfoTerbaru);
        rvDaftarInfoTerbaru.setHasFixedSize(true);
        rvDaftarInfoTerbaru.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDaftarInfoTerbaru.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        //Assign Activity this to progress dialog
        progress = new ProgressDialog(getContext());
        //setting up message in progress dialog
        progress.setMessage("Loading Info Terbaru");
        //Showing progress dialog.
        progress.show();
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("info_terbaru");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    info_terbaru dk = postSnapshot.getValue(info_terbaru.class);
                    infoTerbaruList.add(dk);
                }

                FirebaseRecyclerAdapter<info_terbaru,InfoTerbaruAdapter.ViewHolder>
                        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<info_terbaru,InfoTerbaruAdapter.ViewHolder>
                        (info_terbaru.class,R.layout.row_info_terbaru,InfoTerbaruAdapter.ViewHolder.class,db) {
                    @Override
                    protected void populateViewHolder(InfoTerbaruAdapter.ViewHolder viewHolder, info_terbaru model, int position) {
                        final String post_keyInfo = getRef(position).getKey();
                        info_terbaru infoTerbaru = infoTerbaruList.get(position);
                        viewHolder.txtJudulGambar.setText(infoTerbaru.getJudul_gambar());
//                        viewHolder.textHarga.setText("Rp. "+ String.valueOf(dataKost.getHargaPerBulan())+ ".000,-");

                        Glide.with(InfoTerbaruFragment.this).load(infoTerbaru.getInfo_gambar()).into(viewHolder.imageViewInfo);

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                Toast.makeText(getContext(),post_key, Toast.LENGTH_SHORT).show();

                                Intent deskripsiIntentInfo = new Intent(getContext(), DetailInfoTerbaruActivity.class);
                                deskripsiIntentInfo.putExtra("info_id",post_keyInfo);
                                startActivity(deskripsiIntentInfo);

                            }
                        });
                    }
                };

//                mAdapter = new OneFragmentAdapter(firebaseRecyclerAdapter);

                rvDaftarInfoTerbaru.setAdapter(firebaseRecyclerAdapter);

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
        fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new TambahInfoTerbaruFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.screen_area,fragment);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
