package com.example.hi.tugasakhirproyek;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hi.tugasakhirproyek.Model.kost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;

public class UpdateKostActivity extends AppCompatActivity {
    //Folder Path for firebase storage
    Button btSubmit;
    EditText txtStokKamar;
    StorageReference storageReferences;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    private String mPost_Key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_kost);

        storageReferences = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("kost");
        mPost_Key = getIntent().getExtras().getString("detail_id");


        btSubmit = (Button)findViewById(R.id.btSubmitUpdate);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateDataKos();
            }
        });

        txtStokKamar = (EditText)findViewById(R.id.txtStokKamarUpdate);

        mPost_Key = getIntent().getExtras().getString("detail_id");
        databaseReference.child(mPost_Key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer stokKostValue = Integer.parseInt((String) dataSnapshot.child("stok_kamar").getValue().toString());
                txtStokKamar.setText(String.valueOf(stokKostValue));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateKostActivity.this, MainActivity.class));
            }
        });
    }


    public void UpdateDataKos(){

        databaseReference = FirebaseDatabase.getInstance().getReference().child("kost");
        mPost_Key = getIntent().getExtras().getString("detail_id");
        Integer stok_kamar = Integer.parseInt(txtStokKamar.getText().toString().trim());

        databaseReference.child(mPost_Key).child("stok_kamar").setValue(stok_kamar);

        //showing toast message after done uploading.
        Toast.makeText(getApplicationContext(), "Update Data Stok Kost Successfully",Toast.LENGTH_LONG).show();
        startActivity(new Intent(UpdateKostActivity.this, MainActivity.class));
    }

}

