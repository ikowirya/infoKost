package com.example.hi.tugasakhirproyek;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hi.tugasakhirproyek.Fragment.DetailMapsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class DetailDaftarKostActivity extends AppCompatActivity {

    DetailMapsFragment homeFragment = new DetailMapsFragment();
    private String mPost_Key = null;
    private DatabaseReference mDatabase;
    private ImageView imageView;
    private Button btHapus;
    private FirebaseAuth mAuth;
    private TextView tvNamaKost, tvJenisKost, tvStokKamar, tvLuasKamar,tvDeskripsi,tvNoTelpPemilik,tvBiayaSewa,tvAlamat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_daftar_kost);

        final Toolbar toolbar = (Toolbar)findViewById(R.id.MyToolbar);
        btHapus = (Button) findViewById(R.id.btHapus);
        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapse_toolbar);
        collapsingToolbarLayout.setTitle(" ");

        Context context = this;

//        toolbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent deskripsiIntent = new Intent(DetailDaftarKostActivity.this, MainActivity.class);
//                startActivity(deskripsiIntent);
//            }
//        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("kost");
        mPost_Key = getIntent().getExtras().getString("kos_id");

        tvNamaKost = (TextView)findViewById(R.id.tvNamaKost);
        tvJenisKost = (TextView)findViewById(R.id.tvJenisKost);
        tvStokKamar = (TextView)findViewById(R.id.tvStokKamar);
        tvLuasKamar = (TextView)findViewById(R.id.tvLuasKamar);
        tvDeskripsi = (TextView)findViewById(R.id.tvDeskripsi);
        tvNoTelpPemilik = (TextView)findViewById(R.id.tvNoTelpPemilik);
        tvBiayaSewa = (TextView)findViewById(R.id.tvBiayaSewa);
        tvAlamat = (TextView)findViewById(R.id.tvAlamat);
        imageView = (ImageView)findViewById(R.id.imageView);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.maps,homeFragment);
        fragmentTransaction.commit();

        mDatabase.child(mPost_Key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_tvNamaKost = (String) dataSnapshot.child("nama_kost").getValue();
                String post_tvJenisKost = (String) dataSnapshot.child("jenis_kost").getValue();
                int post_tvStokKamar = Integer.parseInt((String) dataSnapshot.child("stok_kamar").getValue().toString());
                String post_tvLuasKamar = (String) dataSnapshot.child("luas_kamar").getValue();
                String post_tvDeskripsi = (String) dataSnapshot.child("deskripsi").getValue();
                String post_tvNoTelpPemilik = (String) dataSnapshot.child("notelp_pemilik").getValue();
                int post_tvBiayaSewa = Integer.parseInt((String) dataSnapshot.child("biaya_sewa").getValue().toString());
                String post_tvAlamat = (String) dataSnapshot.child("alamat_kost").getValue();
                String post_imageView = (String) dataSnapshot.child("foto").getValue();

                tvNamaKost.setText(post_tvNamaKost);
                tvJenisKost.setText(post_tvJenisKost);
                tvStokKamar.setText("Sisa " + String.valueOf(post_tvStokKamar) + " Kamar");
                tvLuasKamar.setText(post_tvLuasKamar);
                tvDeskripsi.setText(post_tvDeskripsi);
                tvNoTelpPemilik.setText(post_tvNoTelpPemilik);
                tvAlamat.setText(post_tvAlamat);
                tvBiayaSewa.setText("Rp " + String.valueOf(post_tvBiayaSewa));

                Picasso.with(DetailDaftarKostActivity.this).load(post_imageView).into(imageView);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(mPost_Key).removeValue();

            }
        });

    }



}
