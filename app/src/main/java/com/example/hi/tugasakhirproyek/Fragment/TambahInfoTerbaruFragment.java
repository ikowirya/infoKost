package com.example.hi.tugasakhirproyek.Fragment;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.hi.tugasakhirproyek.Model.info_terbaru;
import com.example.hi.tugasakhirproyek.R;
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
 * Created by Hi on 20/01/2018.
 */

public class TambahInfoTerbaruFragment extends Fragment{
    String Storage_Path = "Foto_Url/";
    Button btPilihCover, btSubmit;
    ImageView imageCover;
    EditText txtJudulGambar;
    Uri FilePathUri;
    StorageReference storageReferences;
    DatabaseReference databaseReference;
    List<info_terbaru> infoTerbaruList;
    int Image_Request_Code = 7;
    TextView tvAddress,tvAddress2;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tambah_info_terbaru,null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        storageReferences = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("info_terbaru");
        infoTerbaruList = new ArrayList<>();

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
                InsertDataInfoTerbaru();
            }
        });

        imageCover = (ImageView)view.findViewById(R.id.imageCover);
        txtJudulGambar = (EditText)view.findViewById(R.id.txtJudulGambar);
        progressDialog = new ProgressDialog(getActivity());
    }

    private void InsertDataInfoTerbaru() {
        if (FilePathUri != null){

            //Setting Progres dialog title
            progressDialog.setTitle("Insert Data Info Terbaru");

            //Show Progress dialog
            progressDialog.show();

            //Creating second storage reference
            StorageReference storageReference2nd = storageReferences.child(Storage_Path + System.currentTimeMillis()+"."+GetFileExtension(FilePathUri));

            //Adding addOnSuccessListener to second StorageReference
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String judul_gambar = txtJudulGambar.getText().toString().trim();

                            if (!TextUtils.isEmpty(judul_gambar)){
                                //Get image upload ID
                                String id_info = databaseReference.push().getKey();

                                @SuppressWarnings("VisibleForTests")
                                info_terbaru dk = new info_terbaru(id_info, taskSnapshot.getDownloadUrl().toString(),judul_gambar);

                                //Adding image upload id child element into databaseRefence
                                databaseReference.child(id_info).setValue(dk);

                                //Hiding the progressDialog after done uploading
                                progressDialog.dismiss();

                                //showing toast message after done uploading.
                                Toast.makeText(getActivity(), "Insert Data Info Terbaru Successfully",Toast.LENGTH_LONG).show();
//                                startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
                                Fragment fragment = new InfoTerbaruFragment();
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.screen_area,fragment);
                                fragmentTransaction.commit();

                            }else {
                                Toast.makeText(getActivity().getApplicationContext(), "Please Enter Data Info Terbaru",Toast.LENGTH_LONG).show();
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
                            progressDialog.setTitle("Insert Data Info Terbaru ...");
                        }
                    });
        }
        else {
            Toast.makeText(getActivity(), "Please Select Image or Add Data Info Terbaru",Toast.LENGTH_LONG).show();
        }
    }

    private String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        //Returning the file extension
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
}
