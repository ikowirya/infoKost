package com.example.hi.tugasakhirproyek;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailInfoTerbaruActivity extends AppCompatActivity {

    private String mPost_KeyInfo = null;
    private DatabaseReference mDatabase;
    private ImageView imageViewInfo;
    private TextView txtJudulGambar;
    private Button btHapus;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info_terbaru);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        btHapus = (Button) findViewById(R.id.btHapus);
        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbarLayout.setTitle(" ");

        Context context = this;

//        toolbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent deskripsiIntent = new Intent(DetailDaftarKostActivity.this, MainActivity.class);
//                startActivity(deskripsiIntent);
//            }
//        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("info_terbaru");
        mPost_KeyInfo = getIntent().getExtras().getString("info_id");

        txtJudulGambar = (TextView) findViewById(R.id.txtJudulGambar);
        imageViewInfo = (ImageView) findViewById(R.id.imageViewInfo);

        mDatabase.child(mPost_KeyInfo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_tvJudulGambar = (String) dataSnapshot.child("judul_gambar").getValue();
                String post_imageViewInfo = (String) dataSnapshot.child("info_gambar").getValue();

                txtJudulGambar.setText(post_tvJudulGambar);
                Picasso.with(DetailInfoTerbaruActivity.this).load(post_imageViewInfo).into(imageViewInfo);

                if (mAuth.getCurrentUser().getUid().equals(post_tvJudulGambar)){
                    btHapus.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(mPost_KeyInfo).removeValue();
                Intent deskripsiIntent = new Intent(DetailInfoTerbaruActivity.this, MainActivity.class);
                startActivity(deskripsiIntent);
            }
        });

    }
}
