package com.example.hi.tugasakhirproyek;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.example.hi.tugasakhirproyek.Adapter.DaftarKostAdapter;
import com.example.hi.tugasakhirproyek.Model.kost;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CariKostActivity extends AppCompatActivity {
    RecyclerView rvDaftarKost;
    DaftarKostAdapter mAdapter;
    EditText tvCariKost;
    ImageButton btCariKost;
    private List<kost> kostList = new ArrayList<>();
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari_kost);
        tvCariKost =  (EditText) findViewById((R.id.tvCariKost));
        btCariKost=  (ImageButton) findViewById((R.id.btCariKost));
        rvDaftarKost = (RecyclerView) findViewById(R.id.rvDaftarInfoTerbaru);
        rvDaftarKost = (RecyclerView) findViewById(R.id.rvDaftarInfoTerbaru);
        rvDaftarKost.setHasFixedSize(true);
        rvDaftarKost.setLayoutManager(new LinearLayoutManager(this));
        rvDaftarKost.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        //Assign Activity this to progress dialog
        progress = new ProgressDialog(this);
        //setting up message in progress dialog
        progress.setMessage("Loading Daftar Kost");
        //Showing progress dialog.
        progress.show();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CariKostActivity.this, MainActivity.class));
            }
        });



        btCariKost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            String searchText = tvCariKost.getText().toString();
            final DatabaseReference db = FirebaseDatabase.getInstance().getReference("kost");
            final Query firebaseSearch = db.orderByChild("nama_kost").startAt(searchText).endAt(searchText + "\uf8ff");
            firebaseSearch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    kost dk = postSnapshot.getValue(kost.class);
                    kostList.add(dk);
                }

                FirebaseRecyclerAdapter<kost,DaftarKostAdapter.ViewHolder>
                        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<kost,DaftarKostAdapter.ViewHolder>
                        (kost.class,R.layout.row_daftar_kost,DaftarKostAdapter.ViewHolder.class,firebaseSearch) {
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

                        Glide.with(CariKostActivity.this).load(dataKost.getFoto()).into(viewHolder.imageView);



                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                Toast.makeText(getContext(),post_key,Toast.LENGTH_SHORT).show();

                                Intent deskripsiIntent = new Intent(CariKostActivity.this, DetailDaftarKostActivity.class);
                                deskripsiIntent.putExtra("kos_id",post_key);
                                startActivity(deskripsiIntent);

                            }
                        });
                    }
                };

                rvDaftarKost.setAdapter(firebaseRecyclerAdapter);

                //Hiding the progress dialog
                progress.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progress.dismiss();
            }
        });
            }
        });
    }
}



