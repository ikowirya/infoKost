package com.example.hi.tugasakhirproyek.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hi.tugasakhirproyek.Model.kost;
import com.example.hi.tugasakhirproyek.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Hi on 18/01/2018.
 */

public class TambahPropertiFragment extends Fragment {

    //Folder Path for firebase storage
    String Storage_Path = "Foto_Url/";
    Button btPilihCover, btSubmit;
    ImageView imageCover;
    EditText txtNamaKost, txtStokKamar, txtBiayaSewa, txtLuasKamar, txtNoTelpPemilik, txtDeskripsi;
    Spinner spinnerJenisKost;
    Uri FilePathUri;
    StorageReference storageReferences;
    DatabaseReference databaseReference;
    List<kost> kostList;
    int Image_Request_Code = 7;
    TextView tvAddress,tvAddress2;
    ProgressDialog progressDialog;

    int PLACE_PICKER_REQUEST = 1;
    String lat,lang;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tambah_properti,null);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            Place place = PlacePicker.getPlace(getActivity(), data);
            tvAddress2.setText(place.getAddress());
            tvAddress.setText(place.getLatLng().toString());
            String latLang=tvAddress.getText().toString().trim();

            int index, index2,index3;
            index = latLang.indexOf("(") + 1;
            index2= latLang.indexOf(",");
            index3= latLang.indexOf(")");

            lat = latLang.substring(index, index2);
            lang = latLang.substring(index2 + 1, index3);

//                tvAddress.setText(lat);
//                tvAddress2.setText(lang);

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null ){
            FilePathUri = data.getData();
            try {
                //Getting Selected image into bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),FilePathUri);

                //Setting up bitmap selected image into image view
                imageCover.setImageBitmap(bitmap);

                //after selecting image change choose button above text
                btPilihCover.setText("Image Selected");
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        btSubmit = (Button)view.findViewById(R.id.btSubmit);
//        btSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment fragment = new MapsFragment();
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.screen_area,fragment);
//                fragmentTransaction.commit();
//            }
//        });

        storageReferences = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("kost");
        kostList = new ArrayList<>();

        btPilihCover = (Button)view.findViewById(R.id.btPilihCover);
        btPilihCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
            }
        });

        btSubmit = (Button)view.findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertDataKost();
            }
        });

        imageCover = (ImageView)view.findViewById(R.id.imageCover);
        txtNamaKost = (EditText)view.findViewById(R.id.txtNamaKost);
        txtStokKamar = (EditText)view.findViewById(R.id.txtStokKamar);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        txtBiayaSewa = (EditText)view.findViewById(R.id.txtBiayaSewa);
        txtLuasKamar = (EditText)view.findViewById(R.id.txtLuasKamar);
        tvAddress2 = (TextView)view.findViewById(R.id.tvAddress2);
        txtNoTelpPemilik = (EditText)view.findViewById(R.id.txtNoTelpPemilik);
        txtDeskripsi = (EditText)view.findViewById(R.id.txtDeskripsi);

        spinnerJenisKost = (Spinner)view.findViewById(R.id.spinnerJenisKost);
        tvAddress = (TextView)view.findViewById(R.id.tvAddress);
        tvAddress2 = (TextView)view.findViewById(R.id.tvAddress2);
        Button pickerButton = (Button)view.findViewById(R.id.pickerButton);
        pickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        progressDialog = new ProgressDialog(getActivity());
    }

    public String GetFileExtension (Uri uri){

        ContentResolver contentResolver = getActivity().getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        //Returning the file extension
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void InsertDataKost() {
        if (FilePathUri != null){

            //Setting Progres dialog title
            progressDialog.setTitle("Insert Data Properti Kost");

            //Show Progress dialog
            progressDialog.show();

            //Creating second storage reference
            StorageReference storageReference2nd = storageReferences.child(Storage_Path + System.currentTimeMillis()+"."+GetFileExtension(FilePathUri));

            //Adding addOnSuccessListener to second StorageReference
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String nama_kost = txtNamaKost.getText().toString().trim();
                            String luas_kamar = txtLuasKamar.getText().toString().trim();
                            Integer stok_kamar = Integer.parseInt(txtStokKamar.getText().toString().trim());
                            Integer biaya_sewa = Integer.parseInt(txtBiayaSewa.getText().toString().trim());
                            String jenis_kost = spinnerJenisKost.getSelectedItem().toString();
                            String deskripsi = txtDeskripsi.getText().toString().trim();
                            String alamat_kost = tvAddress2.getText().toString().trim();
                            String notelp_pemilik = txtNoTelpPemilik.getText().toString().trim();
                            double latitude = Double.parseDouble(lat);
                            double longitude = Double.parseDouble(lang);
                            if (!TextUtils.isEmpty(nama_kost)){
                                //Get image upload ID
                                String id_kost = databaseReference.push().getKey();

                                @SuppressWarnings("VisibleForTests")
                                kost dk = new kost(id_kost, nama_kost,jenis_kost, notelp_pemilik,deskripsi,luas_kamar,alamat_kost, taskSnapshot.getDownloadUrl().toString(), stok_kamar, biaya_sewa, latitude,longitude );

                                //Adding image upload id child element into databaseRefence
                                databaseReference.child(id_kost).setValue(dk);

                                //Hiding the progressDialog after done uploading
                                progressDialog.dismiss();

                                //showing toast message after done uploading.
                                Toast.makeText(getActivity(), "Insert Data Properti Kost Successfully",Toast.LENGTH_LONG).show();
//                                startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
                                Fragment fragment = new DaftarKostFragment();
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.screen_area,fragment);
                                fragmentTransaction.commit();
                            }else {
                                Toast.makeText(getActivity().getApplicationContext(), "Please Enter Data Properti Kost",Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Hiding the progressDialog
                            progressDialog.dismiss();

                            //Showing exception error message
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //Setting progress dialog title
                            progressDialog.setTitle("Insert Data Properti Kost ...");
                        }
                    });
        }
        else {
            Toast.makeText(getActivity(), "Please Select Image or Add Data Properti Kost",Toast.LENGTH_LONG).show();
        }

    }
}
