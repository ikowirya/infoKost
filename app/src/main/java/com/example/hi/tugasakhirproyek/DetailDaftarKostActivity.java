package com.example.hi.tugasakhirproyek;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.hi.tugasakhirproyek.Fragment.PemesananFragment;
import com.example.hi.tugasakhirproyek.Model.kost;
import com.example.hi.tugasakhirproyek.Model.pemesanan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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
    private DatabaseReference databaseReference;
    private ImageView imageView;
    private Button btHapus, btPemesanan, btUpdateStok;
    private FirebaseAuth mAuth;
    String  email, id_kost, nama_kost, status;
    private TextView tvNamaKost, tvJenisKost, tvStokKamar, tvLuasKamar,tvDeskripsi,tvNoTelpPemilik,tvBiayaSewa,tvAlamat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_daftar_kost);

        final Toolbar toolbar = (Toolbar)findViewById(R.id.MyToolbar);
        btHapus = (Button) findViewById(R.id.btHapus);
        btPemesanan = (Button)findViewById(R.id.btPemesanan);
//        btUpdateStok = (Button)findViewById(R.id.btUpdateStok);
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
        databaseReference = FirebaseDatabase.getInstance().getReference().child(("pemesanan"));
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
                int post_tvStokKamar= Integer.parseInt((String) dataSnapshot.child("stok_kamar").getValue().toString());
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

        btPemesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("YOUR_PREF_NAME", Context.MODE_PRIVATE);

                String email = prefs.getString("email", "");

                reservasi(email, mPost_Key,getIntent().getExtras().getString("nama"), "Belum Konfirmasi");

                mDatabase.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        kost k = dataSnapshot.getValue(kost.class);

                        if (k.getId_kost().toString().trim().equals(mPost_Key)){
                            int jumlahAkhir = Integer.parseInt(String.valueOf(k.getStok_kamar())) - 1;


                            ubahstok(k.getId_kost(), k.getNama_kost(), k.getJenis_kost(), k.getNotelp_pemilik(), k.getDeskripsi(), k.getLuas_kamar(), k.getAlamat_kost(), k.getFoto(), jumlahAkhir, k.getBiaya_sewa(), k.getLatitude(), k.getLongitude());
                            Toast.makeText(DetailDaftarKostActivity.this,"Pesan Kost Sukses",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        btHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(mPost_Key).removeValue();
                Toast.makeText(DetailDaftarKostActivity.this,"Hapus Sukses",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ubahstok(String id_kost, String nama_kost, String jenis_kost, String notelp_pemilik, String deskripsi, String luas_kamar, String alamat_kost, String foto, int stok_kamar, int biaya_sewa, double latitude, double longitude){
        kost k = new kost(id_kost, nama_kost, jenis_kost, notelp_pemilik, deskripsi, luas_kamar, alamat_kost, foto, stok_kamar, biaya_sewa, latitude, longitude);
        mDatabase.child(id_kost).setValue(k);
    }

    private void reservasi (String email, String id_kost, String nama_kost, String status){

        this.email = email;
        this.id_kost = id_kost;
        this.nama_kost = nama_kost;
        this.status = status;
        String id_pemesanan = databaseReference.push().getKey();
        pemesanan p = new pemesanan(id_pemesanan, email, id_kost, nama_kost, status);
        databaseReference.child(id_pemesanan).setValue(p);
    }



}
