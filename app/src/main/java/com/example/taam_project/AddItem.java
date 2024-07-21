package com.example.taam_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class AddItem extends Fragment {
    private EditText lotNumber, name, description;
    private Spinner category, period;
    private FirebaseDatabase db;
    private DatabaseReference itemsRef;
    private StorageReference sb;
    ActivityResultLauncher<Intent> resultLauncher;
    private Uri image;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);
        // grab xml elements
        lotNumber = view.findViewById(R.id.EditLotNumber);
        name = view.findViewById(R.id.EditName);
        category = view.findViewById(R.id.EditCategory);
        period = view.findViewById(R.id.EditPeriod);
        description = view.findViewById(R.id.EditDescription);
        Button submit = view.findViewById(R.id.Submit);
        Button media = view.findViewById(R.id.EditPicture);
        registerResult();

        db = FirebaseDatabase.getInstance("https://cscb07-taam-default-rtdb.firebaseio.com/");
        sb = FirebaseStorage.getInstance().getReference();

        // add spinner elements
        ArrayAdapter<CharSequence> catAdap = ArrayAdapter.createFromResource(getContext(),
                R.array.category_arr, android.R.layout.simple_spinner_item);
        catAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(catAdap);
        ArrayAdapter<CharSequence> perAdap= ArrayAdapter.createFromResource(getContext(),
                R.array.period_arr, android.R.layout.simple_spinner_item);
        perAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        period.setAdapter(perAdap);





        // submit the form
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });



        return view;
    }

    private void addItem(){
        String tmpLot = lotNumber.getText().toString().trim();
        String tmpName = name.getText().toString().trim();
        String tmpCategory = category.getSelectedItem().toString().toLowerCase();
        String tmpPeriod = period.getSelectedItem().toString().toLowerCase();
        String tmpDisc = description.getText().toString().trim();

        if (tmpLot.isEmpty() || tmpName.isEmpty() || tmpCategory.isEmpty() || tmpPeriod.isEmpty() || tmpDisc.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        itemsRef = db.getReference("test/");
       // String id = itemsRef.push().getKey();

        uploadImage(tmpLot);

        Item item = new Item(tmpLot, tmpName, tmpCategory, tmpPeriod, tmpDisc, "");
        itemsRef.child(tmpLot).setValue(item).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                getMediaLink(tmpLot);
                Toast.makeText(getContext(), "Successfully added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to add item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pickImage(){
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);

    }

    private void registerResult(){
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try{
                            image = result.getData().getData();
                        }
                        catch (Exception e){
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );
    }

    private void uploadImage(String lotNumber){
        if (image == null){
            return;
        }
        StorageReference imageRef = sb.child(lotNumber);
        imageRef.putFile(image);
    }

    private void getMediaLink(String tmpLot){
        sb.child(tmpLot).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String link = uri.toString();
                itemsRef.child(tmpLot).child("media").setValue(link);
                }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                Toast.makeText(getContext(), "Failed to process image", Toast.LENGTH_SHORT).show();
            }
        });
    }
}