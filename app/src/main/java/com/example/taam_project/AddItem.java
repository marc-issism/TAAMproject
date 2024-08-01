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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.TimeUnit;


public class AddItem extends Fragment {
    private EditText lotNumber, name, description;
    private Spinner category, period;
    private FirebaseDatabase db;
    private DatabaseReference itemsRef;
    private StorageReference sb;
    ActivityResultLauncher<Intent> resultLauncher;
    private Uri image;
    static public LoadingFragment load = new LoadingFragment();
    static int runTimeCheck = 10;
    private int uploadStat;
    private AlertFragment alert;
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
        uploadStat = 0;
        db = FirebaseDatabase.getInstance("https://cscb07-taam-default-rtdb.firebaseio.com/");
        sb = FirebaseStorage.getInstance().getReference().child("ItemImages/");

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
                Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
                resultLauncher.launch(intent);
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
            alert = AlertFragment.newInstance("Fill out all fields");
            alert.show(getParentFragmentManager(), "alert_fragment");
            return;
        }

        itemsRef = db.getReference("test/");

       // String id = itemsRef.push().getKey();

        int val = uploadImage(tmpLot);

        Item item = new Item(tmpLot, tmpName, tmpCategory, tmpPeriod, tmpDisc, "");
        itemsRef.child(tmpLot).setValue(item).addOnCompleteListener(task -> {
            if (task.isSuccessful() && val == 0) {
                try {
                    getMediaLink(tmpLot, 0);
                    setMediaType(tmpLot, 0);
                    load.show(getParentFragmentManager(), "loading_fragment");

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }



            } else {
                alert = AlertFragment.newInstance("Entry logged, could not upload media");
                alert.show(getParentFragmentManager(), "alert_fragment");
                clearAll();
            }
        });
    }



    private void registerResult(){
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try{
                            image = result.getData().getData();
                            uploadStat = 1;
                        }
                        catch (Exception e){
                            alert = AlertFragment.newInstance("Could not register image");
                            alert.show(getParentFragmentManager(), "alert_fragment");


                        }
                    }
                }
        );
    }

    private int uploadImage(String lotNumber){
        if (uploadStat == 0){
            return -1;
        }
        StorageReference imageRef = sb.child(lotNumber);
        imageRef.putFile(image);
        return 0;
    }

    private void getMediaLink(String lotNum, Integer loop) throws InterruptedException {
        if (loop == runTimeCheck) {
            alert = AlertFragment.newInstance("Media could not be uploaded");
            alert.show(getParentFragmentManager(), "alert_fragment");
            return;
        }

        sb.child(lotNum).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String link = uri.toString();
                itemsRef.child(lotNum).child("media").setValue(link);
                load.dismiss();

                alert = AlertFragment.newInstance("Media successfully uploaded");
                alert.show(getParentFragmentManager(), "alert_fragment");
                clearAll();
//                FragmentManager frag = getParentFragmentManager();
//                FragmentTransaction transaction = frag.beginTransaction();
//                transaction.replace(R.id.fragment_container, HomeFragment.class, null);
//                transaction.commit();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    getMediaLink(lotNum, loop+1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    private void setMediaType(String lot, Integer loop){
        if (loop == runTimeCheck){
            alert = AlertFragment.newInstance("Media could not be uploaded");
            alert.show(getParentFragmentManager(), "alert_fragment");
            return;
        }

        StorageReference item = sb.child(lot);
        item.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                // Metadata now contains the metadata for 'images/forest.jpg'
                String mediaType = storageMetadata.getContentType();
                itemsRef.child(lot).child("mediaType").setValue(mediaType);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    setMediaType(lot, loop+1);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    private void clearAll(){

        lotNumber.setText("");
        name.setText("");
        description.setText("");
        category.setSelection(0);
        period.setSelection(0);
        image = null;
        uploadStat = 0;
    }



}